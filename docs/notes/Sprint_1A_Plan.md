---
tags: [milestone1, sprint1a, planning]
status: CLOSED
---

# 📅 Sprint 1A: Booking & Calendar Integrity (Hardened)

**Goal**: Finalize authoritative business rules, ensure capacity fallback policies, and standardize conflict handling across the physical booking engine.

## ✅ Sprint Status: CLOSED (Verified)
The system has been hardened and verified against the authoritative Firestore logic.

### 🛠 Recent Completion Pass
- [x] **Occupancy Rules**: Added strict rules to `firestore.rules`.
- [x] **Fallback Policy**:
    - Venue: Max 1 slot (Strict).
    - Decorator: Max 4 slots (Fallback).
- [x] **Conflict Standardization**:
    - `CONFLICT_SLOT_OCCUPIED`
    - `CONFLICT_DATE_BLOCKED`
    - `CONFLICT_CAPACITY_FULL`
- [x] **Calendar Alignment**: Derived calendar state now matches the authoritative repository logic exactly.

### 📦 Key Components
- **Repository**: `FirebaseBookingRepositoryImpl.kt` (Atomic transactions + Capacity resolution).
- **Calendar**: `FirebaseCalendarRepositoryImpl.kt` (Conflict-aware derivation).
- **UI Mapping**: `NewBookingViewModel.kt` (Hardened error handling).

## ⚠️ Known Blockers / Deferred
- **Update/Reschedule**: Deferred to Sprint 1C (deferred by user).
- **Manual Data Audit**: Future task to ensure all `VendorProfile` docs have explicit `capacity` (using fallbacks for now).

---
[[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[SYSTEM_STATUS|📊 System Status]]
