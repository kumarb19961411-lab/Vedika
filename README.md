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
- [**Project Hub**](docs/00_Home/Project_Hub.md) — Master Obsidian entry point.
- [**Current Status**](docs/00_Home/Current_Status.md) — **CURRENT**: Phase 3 Backend/Hardening status.
- [**Scope By Phase**](docs/01_Product/Scope_By_Phase.md) — Roadmap and Phase 1-3 definitions.

### 🏙 Architecture & Core
- [**Repo Map**](docs/02_Architecture/Repo_Map.md) — Module map and ownership boundaries.
- [**Vendor Profile Contract**](docs/04_Data_Contracts/Vendor_Profile_Contract.md) — Mapping registration inputs to the vendor shell.

### 🔐 Guides & Workflows
- [**Navigation Map**](docs/02_Architecture/Navigation_Map.md) — Bottom nav, routes, and visibility rules.
- [**Auth Master Workflow**](docs/03_Workflows/Auth_Master_Workflow.md) — Sign In/Up and User/Partner branching.
- [**Regression Guard**](docs/guides/android_build_regression_guard.md) — **CRITICAL**: Build and safety rules.
- [**Emulator Checklist**](docs/guides/emulator_test_checklist.md) — QA verification flows.

### 🛡 History & Governance
- [**Changelog V2**](docs/changelog_v2.md) — Major milestones.
- [**Historical Logs**](docs/archive/) — Archived build fixes and phase snapshots.

---

## 🛠 Documentation Governance

To maintain documentation integrity, please follow these rules:

1. **Canonical Authorities**: The following files in the Obsidian vault are primary sources of truth:
    - `docs/guides/android_build_regression_guard.md` (Build & Dependencies)
    - `docs/02_Architecture/Navigation_Map.md` (Routes & Shell UI)
    - `docs/03_Workflows/Auth_Master_Workflow.md` (Auth logic)
    - `docs/04_Data_Contracts/Vendor_Profile_Contract.md` (Data mapping)
2. **Archival Procedure**: Phase-specific documents should be moved to `docs/archive/` once the phase is locked.
3. **Historical Logs**: Do not delete `androidstudiochanges.md` or `BUILD_WARNING_RESOLUTION.md`.

---
*Vedika V2 — Crafting Auspicious Occasions.*
