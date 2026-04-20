---
tags: [sprint, plan, milestone1, documentation]
---

# 🏃 Sprint 1A: Booking + Calendar Integrity (Planning Only)

**Goal**: Transform the booking engine from mock-logic to an authoritative, profile-derived business system with rigorous conflict validation and calendar consistency.

## 🔍 Current Code Reality Check

### 1. Booking Flow
- **`FirebaseBookingRepositoryImpl`**: Implements a robust `createBooking` flow using a Firestore transaction. It currently checks `COL_BLOCKED_DATES` and `COL_OCCUPANCY`.
- **`NewBookingViewModel`**: Manages the multi-step form. It proactively calls `checkConflicts()` during data entry.
- **Data Reality**: `COL_OCCUPANCY` is used as a shadow-indexing node (`${vendorId}_${date}`) to facilitate fast conflict checks without reading the entire `bookings` collection.

### 2. Gap Analysis
- **Threshold Inconsistency**: Capacity is currently hardcoded (`4`) for decorators in some places. This must be pulled from the `VendorProfile`.
- **Vendor Type Ambiguity**: Some components use `primaryServiceCategory` string parsing while others use `vendorType` enum fields.
- **Occupancy Lifecycle**: Occupancy is released on `CANCELLED` status, but the full lifecycle (especially rescheduling/updates) is not yet transaction-safe.
- **Calendar Logic**: Reactive but uses slightly different derivation logic than the repository's transaction checks.

## 🛠️ File-by-File Implementation Plan

### Core Data Module
- **[[Booking.kt]]**: Audit for field consistency (ensure `slotType` and `status` are non-null).
- **[[FirebaseBookingRepositoryImpl.kt]]**:
    - Fetch `capacity` from `vendorRef` inside `createBooking` transaction.
    - Standardize error messages (e.g., `CONFLICT_SLOT_OCCUPIED`).
    - Harden `updateBookingStatus` to ensure total consistency for `CANCELLED` states.

### Feature Modules
- **[[NewBookingViewModel.kt]]**: 
    - Replace hardcoded category checks with canonical profile-derived enums.
    - Map repository exceptions to user-friendly UI warnings.
- **[[FirebaseCalendarRepositoryImpl.kt]]**: 
    - Unify `deriveDayState` to use the same capacity/slots logic as the repository.

## 📋 Task Checklist & Dependencies

1. [ ] **Model Cleanup**: Align `VendorProfile` and `Booking` serialization. (Dependency: None)
2. [ ] **Repository Hardening**: Implement dynamic capacity resolution in transactions. (Dependency: 1)
3. [ ] **Booking Lifecycle**: Finalize the `CANCELLED` ➔ `Release Occupancy` path. (Dependency: 2)
4. [ ] **ViewModel Alignment**: Connect proactive conflict checks to the new Repository logic. (Dependency: 2)
5. [ ] **Calendar Validation**: Verify `bookings` ➔ `occupancy` ➔ `calendar` consistency. (Dependency: 3)

## ✅ Acceptance Criteria (Sprint 1A)
- [ ] **Authority**: No hardcoded capacity (e.g., "4") remains in the codebase.
- [ ] **Integrity**: Attempting a double-booking on a Venue during high-concurrency (transaction) triggers a specific `SLOT_OCCUPIED` error.
- [ ] **Consistency**: Cancelling a booking immediately clears the corresponding slot in the Calendar view.
- [ ] **UX**: The "Submit" button in the booking form is disabled or provides a warning if `checkConflict` returns `true`.

## 🧪 Verification Plan

### Technical Checks
- **Build**: Ensure `:core:data` and `:feature:dashboard` compile cleanly.
- **Integrity**: Verify `COL_OCCUPANCY` doc IDs match the `${vendorId}_${date}` format in the emulator.

### Manual Verification Path
1. **The Conflict Test**: Create a booking. Try to create another for the same slot. UI must fail with a descriptive error.
2. **The Capacity Trim**: Change a Decorator's capacity to `1` in Firestore. Create a booking. Try to create another for the same date. It must fail.
3. **The Cleanup Test**: Cancel a booking. Verify the `occupancy` document's `jobCount` decrements.

## 📝 Obsidian Docs Sync List
- [ ] **docs/SYSTEM_STATUS.md**: Mark Booking Engine as "Phase 3 Hardened".
- [ ] **docs/architecture/backend_integration_blueprint.md**: Detail the `COL_OCCUPANCY` strategy.
- [ ] **docs/architecture/Firebase_Hub.md**: Reference the new validation rules.
- [ ] **docs/notes/project_status.md**: Progress the Milestone 1 tracker.

---
[[Project_Hub|🏠 Project Hub]] | [[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[Sprint_1A_Plan|Top]]
