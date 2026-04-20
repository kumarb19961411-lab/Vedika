---
tags: [milestone, status, hub-leaf, business-engine]
---

# 🎯 Milestone 1: Core Business Engine

**Goal**: Prepare the Vedika booking and inventory systems for production-scale vendor operations by hardening the synchronization, validation, and dashboard reporting pipelines.

## 🏗️ Infrastructure Context
- **Primary Backend**: [[Firebase_Hub#🏗️ Project Infrastructure|Firebase (vedika-e44be)]]
- **Data Contract**: [[backend_integration_blueprint#4. Canonical Vendor Data Contract|VendorProfile]]

## 🧗 Roadmap & Sprints
This milestone is decomposed into two high-intensity sprints:

- **[x] [[Sprint_1A_Plan|Sprint 1A: Booking & Calendar Integrity]]** (VERIFIED)
    - Focus: Canonical registration path completion + vendor-scoped query logic.
- **[[Sprint_1B_Plan|Sprint 1B: Validation & Analytics]]**
    - Focus: Slot conflict validation + real-time dashboard metrics (booking-derived).

## ✅ Definition of Done
- All vendor dashboard metrics are derived from live `/bookings` collection state.
- Booking creation implements server-side-ready validation (slot/date conflict check).
- Vendor registration flow captures and persists canonical profile fields.
- **[[Milestone_1_QA_Checklist|QA Checklist]]** is 100% green on emulator and real device.

---
[[Project_Hub|🏠 Project Hub]] | [[Phase_Tracker_Hub|⏱️ Phase Tracker]] | [[Milestone_1_Core_Business_Engine|Top]]
