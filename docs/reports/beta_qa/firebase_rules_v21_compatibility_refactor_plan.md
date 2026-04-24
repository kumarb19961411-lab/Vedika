# Firebase Rules V2.1 Compatibility Refactor Plan

This document outlines the architectural changes required in the Android client and backend to safely adopt Firebase Rules V2.1 without violating the new privacy and transaction security boundaries.

## A. Discovery Refactor Plan
Currently, all discovery flows in the app read from the `vendors` collection, which is restricted to owner-only in V2.1.
- **`FirestoreConstants.kt`**: Add `const val COL_PUBLIC_VENDORS = "public_vendors"`.
- **Repository Interface**: Add `getPublicVendorProfile(vendorId: String)` to separate consumer-facing reads from owner-facing reads.
- **Implementation (`FirebaseVendorRepositoryImpl`)**:
  - `getVendorsByCategory()` and `getFeaturedVendors()` will query `FirestorePaths.COL_PUBLIC_VENDORS`.
  - `getPublicVendorProfile()` will read from `public_vendors`.
  - `getVendorProfile()` and `getVendorProfileStream()` will continue to query `vendors` but should only be called by the Vendor application dashboard (auth.uid == vendorId).
- **Mapping**: The document read from `public_vendors` maps perfectly to the existing `VendorProfile` model, as the projection will contain identical public-facing fields (`businessName`, `location`, `galleryImages`, `pricing`, etc.), but will omit sensitive operational fields.

## B. `public_vendors` Seed Strategy
For the Discovery screens to function in the Beta Bug Bash, `public_vendors` must contain data.
- **Required Fields**: `businessName`, `ownerName`, `vendorType`/`primaryServiceCategory`, `location`, `isVerified` (true for featured), `coverImage`, and `capacity`.
- **Strategy Options**:
  1. *Manual Console Import*: Tedious and error-prone.
  2. *Local Admin Script*: Fast, deterministic, repeatable.
  3. *Cloud Function Projection*: Architectural ideal (`onDocumentWritten` trigger on `vendors`).
- **Recommendation for Minimal Beta Path**: Use a **Local Admin Script** (Node.js) or a direct Firestore Import to quickly seed `public_vendors` for testing. The Cloud Function projection can be added post-beta to handle ongoing syncs automatically.
- **Rollback**: To reseed, simply delete the `public_vendors` collection and rerun the script.

## C. Booking Architecture Plan
The Android client currently attempts a complex Firestore transaction across `vendors`, `occupancy`, and `bookings` when a user creates a booking.
- **V2.1 Rule Blockers**: Consumers have no read access to `vendors` or `occupancy`, and no write access to `occupancy`. The current payload also sends forbidden fields like `totalAmount` and `slotType`.
- **Recommendation**: **Move booking creation to a Callable Cloud Function**.
  - *Why*: The Admin SDK in Cloud Functions bypasses security rules, allowing it to perform the complex cross-collection capacity check and occupancy write securely, without exposing `occupancy` or `vendors` to the client.
- **Callable Function Contract (`createBooking`)**:
  - **Inputs**: `vendorId`, `eventDate`, `slotType` (or mapped `eventType`), `guestCount`, `notes`.
  - **Backend Responsibilities**:
    1. Validate `context.auth.uid`.
    2. Read `vendors/{vendorId}` to verify `capacity` and `vendorType`.
    3. Read `blocked_dates` and `occupancy`.
    4. Assert availability logic (prevent double-booking or over-capacity).
    5. Derive `totalAmount` based on vendor pricing (or default to 0 for beta).
    6. Commit transaction: Create `bookings/{id}` and increment `occupancy/{id}`.
- **Android Repository Changes**: `FirebaseBookingRepositoryImpl.createBooking()` will be rewritten to use `Firebase.functions.getHttpsCallable("createBooking").call(payload)`.

## D. Booking Update / Cancel Plan
- **Current Issue**: When a booking is cancelled, the client attempts to decrement the `occupancy` counter. This write will be blocked by V2.1 rules.
- **Recommendation**: Move status updates that affect inventory (Cancellation, Rejection, Confirmation) to a Callable Cloud Function (`updateBookingStatus`).
- **Current Methods Impacted**: 
  - `updateBookingStatus()`: Refactor to call a Cloud Function instead of direct Firestore `transaction.update()`.
  - `checkConflict()`: Can remain client-side ONLY IF rules are relaxed to allow read access to `occupancy`, or it must also become a Cloud Function or simply fail during `createBooking`. For Beta, `createBooking` handles the conflict check atomically, so the client-side pre-check could return `true` optimistically and let the function enforce it.

## E. Minimal Beta Path
To unblock the beta while respecting V2.1 rules, the smallest safe implementation is:
1. **Client**: Update `FirebaseVendorRepositoryImpl` to read `public_vendors` for discovery.
2. **Backend**: Write two Callable Cloud Functions (`createBooking` and `updateBookingStatus`) using TypeScript/Node.js to handle inventory transactions.
3. **Data**: Run a one-time Node.js script to seed `public_vendors`.
4. **Rules**: Deploy the current V2.1 `firestore.rules` and `storage.rules` verbatim. No weakening of rules.

## F. Test Plan
1. **Unit Tests**: Update `FakeBookingRepository` to mimic the latency and result of the Callable function. Ensure `AuthViewModelTest` and discovery tests pass.
2. **Firebase Emulators**: 
   - Deploy the new Cloud Functions to the local Emulator Suite.
   - Run integration tests against the local emulators to verify that `createBooking` succeeds through the function and fails if attempted directly via client SDK.
3. **Gradle Verification**: `./gradlew lintDevDebug testDevDebugUnitTest`.
4. **Physical Device QA**: Run the Discovery -> Detail -> Booking flow on a real device connected to the staging environment to ensure end-to-end latency and UI states (loading spinners) behave correctly with Cloud Functions.

## G. Risks and Rollback
- **Risk**: *Discovery is empty*. If the seed script fails or is forgotten, consumers will see a blank home screen.
- **Risk**: *Cloud Function Cold Starts*. Moving booking logic to the backend may introduce latency during the initial invocation.
- **Risk**: *App Check Enforcement*. If App Check is enabled in the backend but not properly configured with Play Integrity in the debug/beta builds, legitimate beta testers will be blocked from calling the functions.
- **Rollback Plan**: If Cloud Functions prove too complex or delay the beta timeline significantly, the fallback is to revert to the previous Git commit and deploy the legacy "V1.0" rules (which allowed client transactions) temporarily, accepting the security debt until Milestone 5.

## H. Approval Request
This plan requires architectural sign-off before implementation begins. 
**Status:** PARTIALLY EXECUTED (Phase 1 CLOSED AND VERIFIED: Discovery Refactor). Waiting to proceed with Phase 2 (Booking Architecture).
