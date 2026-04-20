---
tags: [milestone1, sprint1b, planning]
status: CLOSED
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
- [x] Refactor `DashboardViewModel` and `DashboardUiState` for authoritative KPI consistency.
- [x] Corrected `totalRevenue` field availability and mapping.

### 2. Inventory Hub
- [x] Fix `InventoryViewModel` repository contract alignment (using `AuthRepository`).
- [x] Repair `InventoryHubScreen` malformed signature and wired back navigation.
- [x] Implement "Add Item" dialog/form persistence with authenticated ID.

### 3. Verification & Compliance
- [x] **Repository Consistency**: Fixed `getCurrentVendorId` and `Result`/`Flow` mismatches.
- [x] **Security Audit**: Verified identity comes from `AuthRepository.getCurrentUserId()`.
- [x] **Docs Sync**: Updated `SYSTEM_STATUS.md`, `project_status.md`, and `Milestone_1_QA_Checklist.md`.

## 🎯 Acceptance Criteria
- Dashboard reflects real booking data (counts and revenue).
- Inventory Hub scales with real data retrieved from Firestore.
- No regression in Sprint 1A hardened flows (Conflict checks).

---
[[Milestone_1_Core_Business_Engine|🎯 Milestone 1 Hub]] | [[project_status|📈 Project Status]]
