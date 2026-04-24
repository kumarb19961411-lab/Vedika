# Firebase Rules V2.1 Booking Architecture Refactor Plan

This document outlines the Phase 2 strategy for migrating the Vedika booking system to backend-authoritative Cloud Functions, satisfying the strict requirements of Firebase Rules V2.1.

## A. Current Booking Flow Audit

**1. `createBooking()`**
- **Action:** Client-side Firestore transaction.
- **Reads:** `blocked_dates/{dateId}`, `vendors/{vendorId}`, `occupancy/{dateId}`.
- **Writes:** Creates `bookings/{id}`, sets/updates `occupancy/{dateId}`.
- **V2.1 Incompatibility:** 
  - Consumers are **denied read** access to `vendors`, `blocked_dates`, and `occupancy`.
  - Consumers are **denied write** access to `occupancy`.
  - The booking payload contains finance fields (`totalAmount`), `slotType`, and `vendorType`, which are explicitly blocked by the V2.1 strict creation allowlist (`hasNoFinanceFields`).

**2. `updateBookingStatus()`**
- **Action:** Client-side Firestore transaction.
- **Reads:** `bookings/{bookingId}`.
- **Writes:** Updates booking status. If the status changes to `CANCELLED`, it decrements `occupancy/{dateId}`.
- **V2.1 Incompatibility:** Consumers cannot write to the `occupancy` collection.

**3. `checkConflict()`**
- **Action:** Client-side availability verification.
- **Reads:** `vendors/{vendorId}`, `occupancy/{dateId}`, `blocked_dates/{dateId}`.
- **V2.1 Incompatibility:** Consumers are denied read access to all three collections.

## B. Cloud Function Design

We will implement three HTTPS Callable Cloud Functions to handle all booking logic, running securely via the Firebase Admin SDK.

**1. `createBooking`**
- **Input:** `vendorId` (string), `customerName` (string), `eventDate` (number), `slotType` (string).
- **Auth:** Requires valid `context.auth.uid`.
- **Validation:** Ensure date is future, verify vendor exists.
- **Transaction Reads:** `vendors/{vendorId}`, `blocked_dates/{dateId}`, `occupancy/{dateId}`.
- **Transaction Writes:** Create `bookings/{id}` (calculating `totalAmount` from vendor pricing securely), update `occupancy/{dateId}`.
- **Expected Success Response:** `{ success: true, bookingId: string }`
- **Error Codes:** `UNAUTHENTICATED`, `FAILED_PRECONDITION` (capacity full, blocked date), `NOT_FOUND`.
- **Idempotency:** A unique `idempotencyKey` can be passed to prevent duplicate bookings on network retries.

**2. `updateBookingStatus`**
- **Input:** `bookingId` (string), `status` (string).
- **Auth:** Requires valid `context.auth.uid`.
- **Validation:** Fetch booking, verify the user is either the `userId` or `vendorId`. Verify the status transition is valid (e.g., PENDING -> CANCELLED).
- **Transaction Reads:** `bookings/{bookingId}`.
- **Transaction Writes:** Update `bookings/{bookingId}` status. If status is `CANCELLED` or `REJECTED`, decrement `occupancy/{dateId}`.
- **Expected Success Response:** `{ success: true }`

**3. `checkAvailability`**
- **Input:** `vendorId` (string), `eventDate` (number), `slotType` (string).
- **Auth:** Optional/Authenticated.
- **Transaction Reads:** `vendors/{vendorId}`, `blocked_dates/{dateId}`, `occupancy/{dateId}`.
- **Expected Success Response:** `{ isAvailable: boolean, reason: string|null }`

## C. Android Client Refactor Plan

1. **`FirebaseBookingRepositoryImpl`**:
   - Strip out all client-side `firestore.runTransaction` logic.
   - Refactor `createBooking` to call `Firebase.functions.getHttpsCallable("createBooking").call(payload)`.
   - Refactor `updateBookingStatus` to call `Firebase.functions.getHttpsCallable("updateBookingStatus").call(payload)`.
   - Refactor `checkConflict` to call `Firebase.functions.getHttpsCallable("checkAvailability").call(payload)`.
2. **Repository Interface**: No changes needed. Existing functions use Kotlin `Result<T>` and Coroutines, which map perfectly to Cloud Function calls.
3. **State Management**: Existing `ViewModel` error handling (surfacing `Result.failure`) will seamlessly interpret Cloud Function exceptions (like `FAILED_PRECONDITION`) and display them to the user via UI states.
4. **Fake/Dev Repository**: `FakeBookingRepository` remains unchanged, as it simulates backend logic in memory.

## D. Booking Rules Alignment

- **Admin SDK Power**: Cloud Functions use the Firebase Admin SDK, which bypasses `firestore.rules`. This allows the backend to securely query `vendors` and `occupancy` and enforce capacity limits without granting the Android client any direct access.
- **Client Security**: With booking logic moved to the backend, the `firestore.rules` can strictly deny client writes to `occupancy` and `blocked_dates`, fully protecting vendor operational data. The strict allowlists in V2.1 for booking creation prevent unauthorized payload injection from compromised clients.

## E. Minimal Beta Implementation

To support the beta launch safely, the minimal implementation includes:
- **Consumer Flow**: Consumers request a booking via the `createBooking` Callable Function.
- **Vendor Flow**: Vendors confirm or reject via the `updateBookingStatus` Callable Function.
- **Occupancy**: Managed entirely by the backend upon booking creation and status updates.
- **Finance**: No finance mutation from the client. `totalAmount` is injected by the backend during creation.

## F. Test Plan

1. **Unit Tests**: Ensure `FakeBookingRepository` behavior remains consistent.
2. **Firebase Emulator Tests**: Write Node.js/Jest tests for the new Cloud Functions running against the local Firestore Emulator Suite to verify capacity logic and transaction isolation.
3. **Android Repository Tests**: Validate that the Android app correctly serializes and deserializes the Callable Function payloads.
4. **Gradle Verification**: `./gradlew clean lintDevDebug testDevDebugUnitTest`.
5. **Physical Device QA**: Run a full end-to-end booking flow (Consumer request -> Vendor confirmation -> Consumer cancellation) using the `no-screenrecording` debug tools to verify UI loading states and latency.

## G. Data Migration / Seed Impact

- **Existing Bookings**: No data migration is required. The schema for `bookings` remains unchanged.
- **Occupancy/Blocks**: Existing occupancy data is structurally identical and does not require migration.
- **Discovery**: The `public_vendors` collection seeded in Phase 1 remains unaffected and does not need reseeding.

## H. Risks and Rollback

- **Cloud Function Cold Starts**: The first execution may take 2-5 seconds. Handled natively by the Android client's loading spinners.
- **Callable Permission Issues**: Ensure `context.auth` checks correctly parse the JWT token.
- **App Check Enforcement**: If App Check is enforced, beta clients without Play Integrity attestations will fail. We must ensure the App Check enforcement mode is "Audit" rather than "Enforced" for the early beta.
- **Emergency Rollback**: If Cloud Functions introduce critical regressions, revert the Android repository changes and deploy legacy "V1.0" Firestore rules to unblock booking operations temporarily.

## I. Approval Request

**Status:** BOOKING REFACTOR PLAN READY FOR REVIEW
