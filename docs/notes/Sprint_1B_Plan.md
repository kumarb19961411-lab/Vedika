---
tags: [sprint, plan, milestone1]
---

# 🏃 Sprint 1B: Validation & Analytics

**Focus**: Implementing business logic constraints and deriving dashboard state from real data.

## 🎯 Objectives
- [ ] **Slot/Date Conflict Validation**
    - Implement a `isSlotAvailable(date, slot)` check in the Repository.
    - Prevent `BookingViewModel` from submitting a booking if the slot is already occupied in the Firestore vendor-scope.
- [ ] **Inventory Scoping Cleanup**
    - Align `InventoryRepository` with the `VendorProfile`.
    - Support per-vendor service/item lists stored as sub-collections or structured arrays.
- [ ] **Real-Time Dashboard Metrics**
    - Refactor `VendorDashboardViewModel` to calculate "Upcoming Bookings" and "Monthly Revenue" by aggregating the live `bookings` stream.
    - Remove placeholder mock counters.
- [ ] **Calendar Consistency**
    - Ensure `CalendarScreen` reflects the same reconciled state as the Dashboard (One truth policy).

## 📝 Background
Sprint 1B brings the "Engine" alive. The application will no longer just store data; it will begin enforcing business rules and providing meaningful insights back to the vendor.

---
[[Project_Hub|🏠 Project Hub]] | [[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[Sprint_1B_Plan|Top]]
