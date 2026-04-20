---
tags: [milestone1, sprint1b, planning]
status: PLANNED
---

# 📅 Sprint 1B: Metrics + Inventory + Regression Sweep

**Goal**: Transition dashboard and inventory systems to authoritative Firestore state and verify end-to-end vendor operations.

## 📄 Scope
- **Real-Time Dashboards**: Connect Venue/Decorator views to booking analytics (Revenue, Counts).
- **Inventory Hub Connectivity**: Replace mocks with `InventoryRepository` streams.
- **Add Item Flow**: Basic persistence for vendor catalog items.
- **Regression Pass**: Verify registration and booking flows post-hardening.

## 🛠 Planned Work

### 1. Dashboard Metrics (Analytics)
- [x] Refactor `DashboardViewModel` to expose `totalRevenue`, `pendingCount`, `confirmedCount`.
- [x] Update `VenueDashboardScreen` and `DecoratorDashboardScreen` to display these metrics.

### 2. Inventory Hub
- [x] Create `InventoryViewModel` for state management.
- [x] Connect `InventoryHubScreen` to real Firestore collection.
- [x] Implement "Add Item" dialog/form persistence.

### 3. Verification & Compliance
- [/] **Regression Sweep**: OTP -> Registration -> Dashboard flow.
- [x] **Security Audit**: Verify `/inventory` and `/bookings` rules on real documents.
- [x] **Docs Sync**: Update `SYSTEM_STATUS.md` and `Milestone_1_QA_Checklist.md`.

## 🎯 Acceptance Criteria
- Dashboard reflects real booking data (counts and revenue).
- Inventory Hub scales with real data retrieved from Firestore.
- No regression in Sprint 1A hardened flows (Conflict checks).

---
[[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[project_status|📈 Project Status]]
