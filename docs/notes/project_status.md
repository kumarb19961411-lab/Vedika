---
tags: [project, roadmap, status]
---

# 📈 Project Status: Vedika

## Current Sprint: Phase 3.8C - Sprint 1B Preparation (Inventory & Block-Out)

### ✅ Completed Milestones
- **Firebase Initialization**: Anchored `vedika-e44be`.
- **Auth Foundation**: Role-resolution and session restoration hardened.
- **Completed**: Sprint 1A (Booking & Calendar Integrity)
- **Active**: Sprint 1B (Metrics + Inventory)
- **Status**: Dashboard metrics are now authoritative; Inventory repository connected to UI.
    - Locked fallback policy: **Venue (1), Decorator (4)**.
    - Standardized conflict error codes established.

### 🏗 In Progress
- [x] **Phase 3.8B: Obsidian Brain Hardening**: Documents now strictly connected via MOCs and hub notes.
- [/] **Phase 3.8C: Sprint 1B Preparation**: Designing block-out management and inventory schema.

### 📅 Upcoming
- **Sprint 1B: Inventory & Block-Out Logic**: Core manual block logic and item catalog persistence.
- **Sprint 1C: Rescheduling**: Transitioning existing bookings to new dates atomically.

### 🚩 Known Risks / Blockers
- **Data Migration**: Existing `VendorProfile` documents without `capacity` fields rely on fallbacks. A cleanup task is recommended after Sprint 1B.

---
[[Project_Hub|🏠 Project Hub]] | [[SYSTEM_STATUS|📊 System Status]] | [[Milestone_1_QA_Checklist|✅ QA Checklist]]
