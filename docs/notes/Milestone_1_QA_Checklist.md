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
- [x] Correct fallback capacity applied (Venue: 1, Decorator: 4)
- [x] Standardized `CONFLICT_*` codes in repository logs
- [x] Strictly scoped occupancy read/write in `firestore.rules`

### 📊 Sprint 1B Verification (Metrics & Inventory)
- [x] Dashboard: `totalRevenue` only sums `CONFIRMED` bookings
- [x] Dashboard: `pendingCount` and `confirmedCount` match Firestore exactly
- [x] Inventory: "Add Item" persists to `/inventory` with authenticated `vendorId`
- [x] Inventory: Availability toggle updates live via `InventoryViewModel`
- [/] Regression: OTP flow restores session into hardened Dashboard

## 3. Conflict Validation & Security
- [x] **Verified**: `firestore.rules` enforces vendor isolation on `occupancy` collection.
- [x] **Verified**: `CONFLICT_SLOT_OCCUPIED` triggered correctly for Venues.
- [x] **Verified**: `CONFLICT_DATE_BLOCKED` triggered on manual block creation.
- [x] **Verified**: `CONFLICT_CAPACITY_FULL` triggered for Decorators (exceeding fallback 4).
- [x] **Verified**: Attempting to book an already-booked slot triggers precise UI error.

## 4. Regression Sweep
- [x] Logout -> Login flow preserves role-resolution.
- [x] Splash screen restores session into the correct Vendor Dashboard (not re-triggering registration).

---
[[Project_Hub|🏠 Project Hub]] | [[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[Milestone_1_QA_Checklist|Top]]
