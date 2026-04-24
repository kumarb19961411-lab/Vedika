# Firebase Rules V2.1 App Compatibility Audit

**Status**: 🚨 INCOMPATIBLE (BLOCKS DEPLOYMENT)
**Date**: 2026-04-24

This audit evaluates the Android application's compatibility with the newly drafted Firebase Rules V2.1. The V2.1 rules implement a "Public Discovery, Private Operations" model, strict payload allowlists, and finance field restrictions.

## 1. Discovery Path Mismatch (`vendors` vs `public_vendors`)
- **File**: `core/data/src/main/java/com/example/vedika/core/data/repository/firebase/FirebaseVendorRepositoryImpl.kt`
- **Issue**: The Android app currently uses `FirestorePaths.COL_VENDORS` (`vendors`) for all public discovery methods (e.g., `getVendorsByCategory`, `getFeaturedVendors`). It also uses `getVendorProfile` to load vendor details for consumers.
- **Rule Violation**: In V2.1, `vendors/{vendorId}` is strictly **owner-only**. Consumer reads will fail with `PERMISSION_DENIED`.
- **Required Action**: 
  1. Add `const val COL_PUBLIC_VENDORS = "public_vendors"` to `FirestoreConstants.kt`.
  2. Update discovery methods in `FirebaseVendorRepositoryImpl` to query `public_vendors` instead of `vendors`.
  3. Ensure `getVendorProfile` routes consumers to `public_vendors` while allowing vendors to read their private `vendors` document.

## 2. Booking Creation Transaction Breakdown
- **File**: `core/data/src/main/java/com/example/vedika/core/data/repository/firebase/FirebaseBookingRepositoryImpl.kt`
- **Method**: `createBooking()`
- **Issue**: The app performs a complex client-side transaction to create a booking, which involves checking capacity, reading blocked dates, and updating occupancy counters. This architecture fundamentally violates the V2.1 privacy boundaries.
- **Rule Violations**:
  - **Unauthorized Reads**: The transaction attempts to read `vendors/{vendorId}`, `blocked_dates/{id}`, and `occupancy/{id}`. V2.1 restricts all of these to **owner-only**. Consumers will receive `PERMISSION_DENIED`.
  - **Unauthorized Writes**: The transaction attempts to increment values in `occupancy/{id}`. V2.1 restricts `occupancy` writes to the vendor only.
  - **Payload Allowlists**: The Android client sends fields that are either explicitly forbidden or missing from the V2.1 `create` allowlist:
    - *Forbidden*: `totalAmount` (violates `hasNoFinanceFields` rule).
    - *Not Allowed*: `customerName`, `slotType`, `vendorType`.
    - *Missing*: `userId`, `eventType`.
- **Required Action (Architecture Change)**: 
  - The client-side transaction architecture is completely incompatible with the hardened V2.1 rules. 
  - **Recommendation**: Refactor `createBooking` to use a **Callable Cloud Function**. The client should pass the requested `vendorId`, `date`, and `slotType` to the backend. The backend (using the Admin SDK) can safely perform the multi-document transaction, calculate the `totalAmount` securely, and bypass the client-side rule restrictions.

## 3. Storage Asset Paths
- **Files**: Entire Codebase
- **Issue**: Checking for compatibility with the new `public_assets/` and `private_assets/` storage rules.
- **Status**: **PASS (No Action Required)**. The Android codebase does not currently integrate the Firebase Storage SDK for direct uploads or downloads. Images (cover, gallery) are stored and referenced as URL strings within the Firestore documents.

## Conclusion & Next Steps
**Do not deploy Firebase Rules V2.1 to production.** Doing so will immediately break the Discovery screens (Vendor Browse) and completely break Booking Creation for all consumers.

1. First, execute a code refactor to migrate discovery reads to `public_vendors`.
2. Second, migrate the booking creation logic from a client-side Firestore transaction to a secure Cloud Function.
3. Only after the Android app is updated to match these architectural shifts can the V2.1 rules be safely deployed.
