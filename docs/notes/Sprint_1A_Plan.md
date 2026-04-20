---
tags: [milestone1, sprint1a, planning]
status: CLOSED
---

# 📅 Sprint 1A: Booking & Calendar Integrity (Final Pass)

**Goal**: Establish authoritative, secure, and standardized business rules for the core booking engine.

## ✅ Sprint Status: CLOSED
All verified gaps have been addressed in the final completion pass.

### 🛠 Completed Work
- [x] **Occupancy Rules**: Standardized security rules added to `firestore.rules`.
- [x] **Hardened Fallback Policy**:
    - **Venue**: Capacity = 1 (Strict).
    - **Decorator**: Capacity = 4 (Authoritative fallback).
- [x] **Conflict Standardization**:
    - `CONFLICT_SLOT_OCCUPIED`: Returned on venue slot overlap.
    - `CONFLICT_DATE_BLOCKED`: Returned on manual block.
    - `CONFLICT_CAPACITY_FULL`: Returned on decorator load limit.
- [x] **Logic Alignment**: `FirebaseCalendarRepositoryImpl` now exactly mirrors the booking repository's derivation logic.
- [x] **Security**: `occupancy` collection rules enforced with `${vendorId}_${date}` partition.

### 📦 Deliverables
- `firestore.rules`: Authority grouped by context.
- `FirebaseBookingRepositoryImpl.kt`: Safe resolution with logging.
- `FirebaseCalendarRepositoryImpl.kt`: Enum-based state derivation.

---
[[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[SYSTEM_STATUS|📊 System Status]]
