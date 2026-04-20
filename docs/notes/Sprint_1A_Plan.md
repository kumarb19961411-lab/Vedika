---
tags: [sprint, plan, milestone1]
---

# 🏃 Sprint 1A: Booking & Query Hardening

**Focus**: Locking down the core "Write" and "Read" path for vendor-scoped business data.

## 🎯 Objectives
- [ ] **Hardened Registration Completion**
    - Ensure `VenueRegistrationScreen` and `DecoratorRegistrationScreen` map all UI fields to the `VendorProfile`.
    - Verification: Check Firestore collection `/vendors/{uid}` for full field population.
- [ ] **Vendor-Scoped Booking Queries**
    - Refactor all booking repositories to use `whereEqualTo("vendorId", activeVendorId)` exclusively.
    - Remove hardcoded mock IDs from `BookingRepository` implementation.
- [ ] **Booking Creation Flow**
    - Implement basic `addBooking(booking)` in `BookingRepository` targeting `/bookings`.
    - Ensure newly created bookings appear in the Vendor Dashboard instantly (observing the stream).

## 📝 Background
In this sprint, we shift from "UI Scaffolding" to "Data Integrity." We are treating the `vendorId` as the primary tenant key for all operations.

---
[[Project_Hub|🏠 Project Hub]] | [[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[Sprint_1A_Plan|Top]]
