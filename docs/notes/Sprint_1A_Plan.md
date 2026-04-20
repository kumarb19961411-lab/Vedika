---
tags: [sprint, plan, milestone1, documentation]
---

# 🏃 Sprint 1A: Booking + Calendar Integrity

**Goal**: Transform the booking engine from mock-logic to an authoritative, profile-derived business system with rigorous conflict validation and calendar consistency.

## 🔍 Current Code Reality Check

### 1. Booking Flow
- **`FirebaseBookingRepositoryImpl`**: Implements a robust `createBooking` flow using a Firestore transaction. It currently checks `COL_BLOCKED_DATES` and `COL_OCCUPANCY`.
- **`NewBookingViewModel`**: Manages the multi-step form. It proactively calls `checkConflicts()` during data entry.
- **Data Reality**: `COL_OCCUPANCY` is used as a shadow-indexing node (`${vendorId}_${date}`) to facilitate fast conflict checks.

### 2. Gap Analysis & Corrections
- **Scope Lock**: **Sprint 1A covers Create + Cancel hardening only.** "Rescheduling/Update Date" is deferred to Sprint 1C/follow-up due to transaction complexity.
- **Capacity Resolution**: 
    - **Fallback Policy**: If `capacity` is missing or invalid, we use:
        - `VENUE`: 1 (Strict slot isolation)
        - `DECORATOR`: 4 (Conservative industry average)
    - **Validation**: Attempting to use a profile with missing capacity will trigger a warning log in the debug environment.
- **Vendor Type Ambiguity**: Standardizing on `VendorType` enum derived from `primaryServiceCategory`.

## 🛠️ File-by-File Implementation Plan

### Core Data Module
- **[[FirebaseBookingRepositoryImpl.kt]]**:
    - Implement dynamic capacity fetch in `createBooking` transaction with fallbacks.
    - Standardize error constants.
    - Harden `updateBookingStatus(CANCELLED)` to release occupancy.
- **[[FirebaseCalendarRepositoryImpl.kt]]**: 
    - Unify `deriveDayState` to use the same capacity/slots logic as the repository.

### Feature Modules
- **[[NewBookingViewModel.kt]]**: 
    - Connect proactive conflict checks.
    - Map repository exceptions to user-friendly UI warnings.

## 📋 Task Checklist & Dependencies

1. [ ] **Firebase Rules Review**: Verify rules strictly enforce `vendorId == auth.uid` for all mutation paths.
2. [ ] **Repository Hardening**:
    - [ ] Implement dynamic capacity resolution + fallback logic.
    - [ ] Implement occupancy release in `updateBookingStatus(CANCELLED)`.
3. [ ] **ViewModel Alignment**: Connect proactive conflict checks and error mapping.
4. [ ] **Calendar Validation**: Align `deriveDayState` with Repository logic.
5. [ ] **Verification**:
    - [ ] Manual booking collision test.
    - [ ] Cancellation occupancy release test.

## ✅ Acceptance Criteria (Sprint 1A)
- [ ] **No Hardcoding**: All capacity logic is derived from Profile or documented Fallback Policy.
- [ ] **Integrity**: Double-booking a slot triggers `CONFLICT_SLOT_OCCUPIED`.
- [ ] **Consistency**: Cancelling a booking clears indicators in Dashboard and Calendar.

## 🧪 Verification Plan
- **Technical**: Build all modules; verify occupancy doc structure in emulator.
- **Manual**: Test Double-Booking (fail), Capacity Limit (fail), and Cancellation (pass/clear).

---
[[Project_Hub|🏠 Project Hub]] | [[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[Sprint_1A_Plan|Top]]
