# Vedika Final Remediation Plan (Milestone 4 Locked)

This plan outlines the final corrective actions required to move the Vedika platform from its current "Conditional Go" status to a full Beta-ready state. This document serves as the final authority for the Milestone 4 remediation phase.

## Workstream A: Repository Synchronization & README Refresh
- **Objective**: Align project entry points with current architecture and security requirements.
- **Tasks**:
  - Update `README.md` to remove legacy Phase 2/3 references.
  - Fix broken documentation links pointing to `/docs/` subdirectory.
  - Correct/remove OTP bypass documentation (Standardizing on 6-digit production requirements).
  - Update `docs/SYSTEM_STATUS.md` to reflect the final remediation focus.

## Workstream B: Firestore & Storage Security Hardening
- **Objective**: Establish a secure "Public Discovery, Private Data" model.
- **Target Files**:
  - `firestore.rules`: Implement role-based access. Allow public read for `vendors`, but owner-only for sensitive fields. Restrict `bookings` to involved parties.
  - `storage.rules`: Allow public `read` for `/vendors/**` paths to enable discovery image galleries. Maintain `write` restricted to authenticated owners.

## Workstream C: Firebase App Check Integration
- **Objective**: Prevent unauthorized access to backend services.
- **Tasks**:
  - Initialize Firebase App Check in `MainActivity.kt` using `PlayIntegrityProvider`.
  - Verify enforcement in debug mode (using debug tokens).

## Workstream D: View Model Test Remediation (DEF-003)
- **Objective**: Ensure business logic stability for core features.
- **Target Files**:
  - `feature/finance/src/test/.../FinanceViewModelTest.kt`: Revenue logic, sorting, and error handling.
  - `feature/calendar/src/test/.../CalendarViewModelTest.kt`: Date selection, booking matrix, and conflict logic.

## Workstream E: Final Regression & Beta Signoff
- **Objective**: Final validation of build stability and feature integrity.
- **Tasks**:
  - Run full Gradle test suite: `./gradlew testDebugUnitTest`.
  - Execute project-wide Lint: `./gradlew lintDevDebug`.
  - Update `docs/reports/beta_qa/physical_device_qa_gate_summary.md` to reflect full "GO" status after remediation.

---
**Status**: LOCKED
**Owner**: Senior Android Release QA Engineer
**Gate Condition**: Zero critical defects, 100% security rule enforcement.
