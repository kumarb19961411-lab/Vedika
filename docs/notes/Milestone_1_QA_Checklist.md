---
tags: [qa, checklist, milestone1]
---

# ✅ Milestone 1 QA Checklist

This checklist must be satisfied before Milestone 1 is considered "Closed."

## 1. Vendor Registration
- [ ] Profile document created in `/vendors/{uid}`.
- [ ] All required fields (Business Name, Location, Category) present in Firestore.
- [ ] Navigation correctly routes to Dashboard after first-time completion.

## 2. Booking Core
- [ ] Booking successfully saved to `/bookings`.
- [ ] `vendorId` in booking matches active vendor UID.
- [ ] Dashboard counter increments automatically after booking creation.
- [ ] Calendar displays new booking without manual refresh.

## 3. Conflict Validation
- [ ] Attempting to book an already-booked slot for the same date triggers "Slot Unavailable" UI.
- [ ] Repository successfully performs the availability check against Firestore.

## 4. Regression Sweep
- [ ] Logout -> Login flow preserves role-resolution.
- [ ] Splash screen restores session into the correct Vendor Dashboard (not re-triggering registration).

---
[[Project_Hub|🏠 Project Hub]] | [[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[Milestone_1_QA_Checklist|Top]]
