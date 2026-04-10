# Vedika V2 — Premium Android Onboarding

Vedika is a modern, frontend-first Android application designed for event planning (Venues, Catering, Decoration). Version 2 (V2) focuses on a high-fidelity, role-aware authentication and registration experience using Jetpack Compose.

## 🚀 Quick Start

### Build Requirements
- **Android Studio**: Ladybug or later.
- **JDK**: Java 17 (Pinned to JBR in `gradle.properties`).
- **Gradle**: 8.7.2.

### Running the App
1. Open the project in Android Studio.
2. Select the `devDebug` or `stagingDebug` build variant.
3. Run on an emulator or physical device.
4. **Dev Bypass**: Use the "Dev Bypass" button on the Login screen or enter OTP `1234` in OTP Verification to skip real SMS verification.

---

## 📖 Documentation Index

### Core Architecture & Strategy
- [**Project Structure**](docs/project_structure.md) — Module map and ownership boundaries.
- [**V2 Scope**](docs/v2_scope.md) — What's in-scope and planned for the current phase.
- [**Changelog V2**](docs/changelog_v2.md) — Operational history of major V2 milestones.

### Stability & Security
- [**Regression Guard**](docs/android_build_regression_guard.md) — **CRITICAL**: Read before any build or dependency changes.
- [**Android Studio Changes**](androidstudiochanges.md) — Historical log of IDE/Build issues resolved.
- [**Build Resolutions**](docs/BUILD_WARNINGS_RESOLUTION.md) — Log of fixed warnings and configs.

### Features & Implementation
- [**Auth Workflow**](docs/v2_auth_workflow.md) — Logic tree for Sign In vs Sign Up and User vs Partner roles.
- [**Interaction Contract**](docs/v2_phase1_interaction_contract.md) — Expected behavior for every button and field.
- [**Phase 1 Screen Map**](docs/v2_phase1_screen_map.md) — Source-of-truth mapping for implemented screens.

### Testing & QA
- [**Emulator Checklist**](docs/emulator_test_checklist.md) — Step-by-step manual verification flows.

---

## 🛠 Documentation Governance

To maintain documentation integrity during V2 development, please follow these rules:

1. **The "Protected" Trio**: The following files represent the system source of truth and must be updated if navigation or dependencies change:
    - `docs/android_build_regression_guard.md` (Update on any `build.gradle` or Hilt change).
    - `docs/v2_auth_workflow.md` (Update on any `NavHost` or `AuthViewModel` change).
    - `resources/phase1/phase1_manifest.yaml` (Static manifest of screens).
2. **Logs vs Policies**: Do not merge or delete `androidstudiochanges.md` or `BUILD_WARNINGS_RESOLUTION.md`. These are preserved historical logs. Final policies belong in the `Regression Guard`.
3. **Drafting**: New workflow reports or one-off verification audits should be placed in `docs/archive/` once reviewed.

---
*Vedika V2 — Crafting Auspicious Occasions.*
