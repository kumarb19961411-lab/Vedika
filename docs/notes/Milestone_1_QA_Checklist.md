---
tags: [qa, checklist, milestone1]
---

# ✅ Milestone 1 QA Checklist

This checklist must be satisfied before Milestone 1 is considered "Closed."

## 1. Vendor Registration
- [x] Profile document created in `/vendors/{uid}`.
- [x] All required fields (Business Name, Location, Category) present in Firestore.
- [x] Navigation correctly routes to Dashboard after first-time completion.

## 2. Booking Core (Sprint 1A Hardening)
- [x] Booking successfully saved to `/bookings`.
- [x] **Verified**: Transactional occupancy update in `/occupancy`.
- [x] **Verified**: Fallback policy applied (Venue=1, Decorator=4) when capacity field is missing.
- [x] Dashboard counter increments automatically after booking creation.
- [x] Calendar displays new booking with authoritative status coloration.

## 3. Conflict Validation & Security
- [x] **Verified**: `firestore.rules` enforces vendor isolation on `occupancy` collection.
- [x] **Verified**: `CONFLICT_SLOT_OCCUPIED` triggered correctly for Venues.
- [x] **Verified**: `CONFLICT_CAPACITY_FULL` triggered correctly for Decorators (>4 total).
- [x] **Verified**: Attempting to book an already-booked slot triggers "Slot Unavailable" UI message.

## 4. Regression Sweep
- [x] Logout -> Login flow preserves role-resolution.
- [x] Splash screen restores session into the correct Vendor Dashboard (not re-triggering registration).

---
[[Project_Hub|🏠 Project Hub]] | [[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[Milestone_1_QA_Checklist|Top]]
