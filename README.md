# Vedika V2 — Premium Android Onboarding & Vendor Shell

Vedika is a modern, frontend-first Android application designed for event planning (Venues, Catering, Decoration). Version 2 (V2) delivers a high-fidelity, role-aware onboarding experience and a premium Vendor Management Shell.

## 🚀 Quick Start

### Build Requirements
- **Android Studio**: Ladybug or later.
- **JDK**: Java 17 (Pinned and documented in `Regression Guard`).
- **Gradle**: 8.7.2.

### Running the App
1. Open the project in Android Studio.
2. Select the `devDebug` or `stagingDebug` build variant.
3. **Dev Bypass**: Use the "Dev Bypass" button on the Login screen or enter OTP `1234` in OTP Verification to skip real SMS verification.

---

## 📖 Documentation Index

### 🏙 Architecture & Core
- [**Project Structure**](docs/project_structure.md) — Module map and ownership boundaries.
- [**Navigation Shell**](docs/vendor_shell_navigation.md) — Bottom nav, routes, and visibility rules.
- [**V2 Scope**](docs/v2_scope.md) — Current status (Phase 2B Complete) and roadmap.
- [**Changelog V2**](docs/changelog_v2.md) — History of major V2 milestones.

### 🔐 Authentication & Control
- [**Auth Workflow**](docs/v2_auth_workflow.md) — Branching logic for Sign In/Up and User/Partner roles.
- [**Role Behavior Matrix**](docs/role_behavior_matrix.md) — Quick reference for destination logic.
- [**Data Continuity**](docs/mock_data_flow.md) — Mapping registration inputs to the vendor shell.

### 🛡 Stability & QA
- [**Regression Guard**](docs/android_build_regression_guard.md) — **CRITICAL**: Mandatory build and navigation safety rules.
- [**Emulator Checklist**](docs/emulator_test_checklist.md) — Manual verification flows for the onboarding path.
- [**Historical Logs**](docs/archive/) — Archived build issues and phase-specific reports.

---

## 🛠 Documentation Governance

To maintain documentation integrity, please follow these rules:

1. **Canonical Authorities**: The following files are the primary sources of truth and MUST be updated if their corresponding logic changes:
    - `docs/android_build_regression_guard.md` (Build & Dependencies)
    - `docs/vendor_shell_navigation.md` (Routes & Shell UI)
    - `docs/v2_auth_workflow.md` (Auth logic)
    - `docs/mock_data_flow.md` (Data mapping)
2. **Archival Procedure**: Phase-specific "Maps" or "Acceptance" documents (e.g., `v2_phase1_acceptance.md`) should be moved to `docs/archive/` once the phase is locked.
3. **Historical Logs**: Do not delete `androidstudiochanges.md` or `BUILD_WARNING_RESOLUTION.md`. These provide the "Why" behind hardcoded build rules.

---
*Vedika V2 — Crafting Auspicious Occasions.*
