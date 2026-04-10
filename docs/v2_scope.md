# V2 Project Scope — Vedika V2

This document defines the implementation boundaries for the Vedika V2 frontend-first development pass.

## 🎯 Current Focus: Phase 1 (Auth & Onboarding Enhancement)
The primary goal of Phase 1 is to deliver a premium, bug-free, and deterministic authentication and registration journey.

### ✅ In-Scope (Phase 1)
1. **Premium UI Fidelity**: Correcting screen layouts, typography, and "glassmorphism" effects to match V2 designs.
2. **Auth Workflow**: Implementing a distinct Signup vs. Sign In flow with deterministic routing.
3. **State Persistence**: Ensuring phone numbers and flow intent (User/Partner) survive across navigation.
4. **Interaction Polish**: Implementing snackbars, form validation, and reactive button states.
5. **Build Stability**: Protecting the repository from Gradle, Hilt, and Firebase package regressions.
6. **Mock Integration**: Validating all 4 primary flows using `FakeAuthRepository` in `dev` mode.

### ⏳ Future Scope (Phase 2+)
1. **Real Firebase Auth**: Transitioning from `FakeAuthRepository` to actual Firebase Auth providers.
2. **Dashboard Logic**: Implementing actual booking lists and profile management.
3. **Cloud Sync**: Connecting registration forms to Firestore.
4. **Social Login**: Logic for Google/Social sign-in handlers.
5. **Deep Linking**: Routing users directly into specific venues or registration steps.

---

## 🚫 Strictly Out of Scope (Phase 1)
- Modification of Legacy modules (`calendar`, `finance`, `gallery`).
- Implementation of real payment gateways.
- Significant changes to the `VedikaTheme` base tokens (unless requested).
- Backend deployment or CI/CD pipeline modifications.

---
*Note: This scope is driven by the `resources/phase1/phase1_manifest.yaml` which serves as the screen-level source of truth.*
