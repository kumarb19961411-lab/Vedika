---
title: System Status
type: status
status: active
owner: Product Architect
phase: Phase 3
last_updated: 2026-04-20
tags: [status, phase3, overview]
---

# Vedika System Status

## Source of truth
High-level overview of the Vedika architecture implementation status.

## Current implementation

Vedika is currently transitioning into Phase 3: Backend Integration & Hardening. The UI shell for Vendor Onboarding and Auth flows have been structurally scouted and scaffolded, but a significant portion of production API syncing remains incomplete. The current objective is to harden data contracts before migrating to real endpoints.

### Fully Implemented
- **V2 Jetpack Compose App Shell**: Fully responsive and modularized architecture (`app`, `feature:auth`, `core:ui`). See [[repo_map]].
- **Obsidian Vault Framework**: Canonical documentation hierarchy correctly reflects repo boundaries.

### Partially Implemented / Scaffolding
- **Vendor Registration Flow (UI)**: Built to support Venues and Decorators visually, but currently writes into mock memory (`MockRepository`) instead of live Firestore.
- **OTP Verification Flow**: UI components exist, but it bypassing actual Firebase SMS logic for local `1234` defaults. See [[auth_master_workflow]].

## Future work

### Planned / Missing Integrations (Next Actions)
1. **Firebase Authentication Sync**: Connect SMS OTP module to live Firebase config to replace the mock bypass logic securely.
2. **Form Data Submission**: Submitting onboarding profiles (Venues/Decorators) matching the schemas outlined in `backend_sync_contract.md` into `vendors/{uid}`.
3. **Session Restoration (`SplashViewModel`)**: Enforcing encrypted session tokens to skip repeated SMS loops entirely. See [[session_restoration]].
4. **Role Determinism**: Implementing `role_behavior_matrix` lookups to strictly boundary users vs vendors on the backend.

### Blockers
Data contracts need rigorous validation against actual `firestore.rules`. See [[open_blockers]].
