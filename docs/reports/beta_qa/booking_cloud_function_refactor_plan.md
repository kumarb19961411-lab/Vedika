# Firebase Rules V2.1 Booking Architecture Refactor Plan V2

This document outlines the revised Phase 2 strategy for migrating the Vedika booking system to backend-authoritative Cloud Functions, satisfying the strict requirements of Firebase Rules V2.1.

## A. Revised V2 Plan Summary

The client-side `FirebaseBookingRepositoryImpl` performs complex transactions across `vendors`, `blocked_dates`, `occupancy`, and `bookings` which violate the new V2.1 privacy boundaries. Specifically, consumers cannot read vendor or occupancy operational data, and no client is allowed to write to occupancy or inject finance fields (`totalAmount`).

To resolve this safely, we will migrate booking lifecycle operations to Firebase Callable Cloud Functions. This ensures operations execute with the Firebase Admin SDK (bypassing rules natively) while strictly enforcing input contracts, capacities, and preventing unauthorized finance mutation or duplicate booking attempts.

## B. Implementation Phasing (Phase 2A / 2B / 2C)

To minimize risk and simplify review, implementation is split into smaller phases:
- **Phase 2A**: Implement the `createBooking` Callable Function + Android repository refactor.
- **Phase 2B**: Implement the `updateBookingStatus` Callable Function + Android repository refactor.
- **Phase 2C (Optional)**: Implement the `checkAvailability` Callable Function *only* if the current UI explicitly requires a pre-check. For the initial beta, `createBooking` will serve as the authoritative availability check.

## C. Phase 2A: `createBooking` API Contract

**1. Input Schema:**
- `vendorId` (string)
- `eventDate` (number)
- `eventType` (string)
- `guestCount` (number)
- `slotType` (string) *[optional, only if needed for venue capacity logic]*
- `notes` (string)
- `idempotencyKey` (string) *[to prevent duplicate bookings on retry]*

*Note: The `customerName` must not be trusted from the client payload. The backend will derive it from `users/{auth.uid}`.*

**2. Backend Responsibilities:**
- Validate `context.auth.uid`.
- Validate input types and allowed enums.
- Read `users/{uid}` for a safe customer snapshot (for `customerName`).
- Read `vendors/{vendorId}` to verify vendor existence and capacity constraints.
- Read `blocked_dates/{vendorId_eventDate}` to ensure the date is not blocked.
- Read `occupancy/{vendorId_eventDate}`.
- Enforce venue slot overlap logic and decorator/service capacity logic.
- Derive or defer `totalAmount` server-side (do not trust client pricing).
- Create `bookings/{bookingId}`.
- Update `occupancy/{vendorId_eventDate}`.
- Support `idempotencyKey` to prevent duplicate transactions.
- Return stable error codes: `UNAUTHENTICATED`, `INVALID_ARGUMENT`, `NOT_FOUND`, `FAILED_PRECONDITION` (capacity/overlap), `ALREADY_EXISTS` (idempotency), `INTERNAL`.

## D. Phase 2B: `updateBookingStatus` API Contract

**1. Input Schema:**
- `bookingId` (string)
- `status` (string)
- `reason` or `vendorNotes` (string) *[if applicable]*
- `idempotencyKey` (string) *[optional, if needed]*

**2. Validation Rules & Responsibilities:**
- Consumers can only cancel their **own** `PENDING` bookings.
- Vendors can transition their **own** bookings to `CONFIRMED`, `REJECTED`, or `CANCELLED`.
- The update **must not** allow reassignment of `vendorId` or `userId`.
- The update **must not** allow mutation of client finance, payout, or settlement fields.
- `occupancy` must be released *only once* when transitioning to `CANCELLED` or `REJECTED` from an active reserved state.
- Define idempotency behavior for repeated cancellation or confirmation requests.

## E. Android Refactor Scope

- `FirebaseBookingRepositoryImpl` will use the Firebase Functions SDK (`Firebase.functions.getHttpsCallable`).
- Ensure the appropriate Gradle dependency (`com.google.firebase:firebase-functions-ktx`) is added.
- Keep the `BookingRepository` interface unchanged (continue using `suspend` and `Result<T>`).
- Map `HttpsCallableException` codes to `Result.failure` using user-friendly domain errors (e.g., mapping `FAILED_PRECONDITION` to a localized capacity message).
- `FakeBookingRepository` will continue to simulate backend results. It will only simulate latency if explicitly needed for testing UI states.

## F. Test Plan

- **Cloud Function Unit Tests**: Test logic isolation and parameter validation.
- **Firestore Emulator Transaction Tests**: Run end-to-end booking flow locally to test capacity lock and overlap enforcement.
- **Android Repository Tests/Mocks**: Verify Android client maps error codes and payloads correctly.
- **Gradle Checks**: `clean assemble`, `test`, `lint`.
- **Physical-Device QA**: Rerun the end-to-end booking flow on a real device using the `no-screenrecording` debug tool.
- **Duplicate Request Tests**: Verify the `idempotencyKey` prevents double billing/booking.
- **Conflict/Capacity Tests**: Attempt to overbook a decorator or double-book a venue slot to trigger `FAILED_PRECONDITION`.
- **Unauthorized Tests**: Attempt to cancel another user's booking or execute `createBooking` without authentication.

## G. Rollback Plan

- **Preferred Rollback**: Since V2.1 rules are not deployed yet, the primary rollback strategy is to revert the booking function/client branch entirely. We will keep V2.1 rules undeployed until the Functions are perfectly stable.
- **Emergency Fallback**: Do not rely on legacy relaxed rules. They are strictly emergency-only, internal-only, and time-boxed. Reverting the git commit is the authoritative rollback path.

## H. Approval Request

**Final Status:** BOOKING REFACTOR PLAN V2 READY FOR REVIEW
