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
- [**System Status**](docs/SYSTEM_STATUS.md) — CURRENT: Phase 3 Backend Integration.
- [**Repo Map**](docs/architecture/repo_map.md) — Module map and feature ownership boundaries.
- [**Auth Master Workflow**](docs/architecture/auth_master_workflow.md) — Sign In/Up and User/Partner deterministic branching.
- [**Backend Sync Contract**](docs/architecture/backend_sync_contract.md) — Firestore data contracts.
- [**Session Restoration**](docs/architecture/session_restoration.md) — JWT cache and zero-latency app launch mechanisms.
- [**Backend Blueprint**](docs/architecture/backend_integration_blueprint.md) — Setup details for Phase 3 DB integration.
- [**Role Behavior Matrix**](docs/architecture/role_behavior_matrix.md) — Quick reference for state routing and graph clearance.

### 🔐 Guides & Workflows
- [**User Sign Up Guide**](docs/guides/user_signup_implementation_guide.md)
- [**User Sign In Guide**](docs/guides/user_signin_implementation_guide.md)
- [**Vendor Sign In Guide**](docs/guides/vendor_signin_implementation_guide.md)
- [**Session Restoration Guide**](docs/guides/session_restoration_guide.md)
- [**User Onboarding Guide**](docs/guides/user_onboarding_guide.md)
- [**Vendor Onboarding Guide**](docs/guides/vendor_onboarding_guide.md)
- [**Role Resolution Guide**](docs/guides/role_resolution_guide.md)

### 🛡 History & Governance
- [**Regression Guard**](docs/android_build_regression_guard.md) — **CRITICAL**: Build and safety rules.
- [**Changelog V2**](docs/changelog_v2.md) — Major milestones.
- [**Historical Logs**](docs/archive/README.md) — Archived build fixes and obsolete phase files.

---

## 🛠 Documentation Governance

To maintain documentation integrity, please follow these strict rules:

1. **Canonical Authorities**: The architecture and guides outlined above must reflect actual source tree behavior. If the code deviates, update these documents immediately.
2. **Archival Procedure**: Phase-specific documents should be moved to `docs/archive/` once the phase is permanently locked. No active `README.md` links should ever point inside the archive.

---
*Vedika V2 — Crafting Auspicious Occasions.*
