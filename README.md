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

### 🚀 Development & Status
- [**System Status**](docs/SYSTEM_STATUS.md) — **CURRENT**: Phase 3 Backend/Hardening status.
- [**V2 Scope**](docs/v2_scope.md) — Roadmap and Phase 1-2 lock status.

### 🏙 Architecture & Core
- [**Project Structure**](docs/architecture/project_structure.md) — Module map and ownership boundaries.
- [**Data Continuity**](docs/architecture/mock_data_flow.md) — Mapping registration inputs to the vendor shell.

### 🔐 Guides & Workflows
- [**Navigation Shell**](docs/guides/vendor_shell_navigation.md) — Bottom nav, routes, and visibility rules.
- [**Auth Workflow**](docs/guides/v2_auth_workflow.md) — Sign In/Up and User/Partner branching.
- [**Regression Guard**](docs/guides/android_build_regression_guard.md) — **CRITICAL**: Build and safety rules.
- [**Emulator Checklist**](docs/guides/emulator_test_checklist.md) — QA verification flows.

### 🛡 History & Governance
- [**Changelog V2**](docs/changelog_v2.md) — Major milestones.
- [**Historical Logs**](docs/archive/) — Archived build fixes and phase snapshots.

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
