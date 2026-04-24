# 🐞 Defect Log - Vedika Beta Readiness

| ID | Component | Severity | Description | Status | Fix/Mitigation |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **DEF-001** | Navigation | High | Deep links to `/vendor/{id}` lost on auth redirect. | ✅ FIXED | `getResolvedRoute` implemented in `MainActivity`. |
| **DEF-002** | Dashboard | Med | Dashboard unit tests failing due to `VendorUser` model mismatch. | ✅ FIXED | Mock data and tests updated. |
| **DEF-003** | Test Coverage | Low | Missing unit tests for calendar and finance modules. | ⏳ PENDING | Deferred to Beta 2 (Post-Physical QA). |
| **DEF-004** | Inventory | Med | `InventoryViewModelTest` missing mocks for conflict checks. | ✅ FIXED | Added `checkConflict` and `isDateBlocked` mocks. |
| **DEF-005** | Security | Med | Storage rules potentially blocking discovery images. | ⏳ PENDING | Requires physical device verification. |
