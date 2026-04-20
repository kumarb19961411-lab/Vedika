---
title: Canonical Auth Master Workflow
type: architecture
status: active
owner: Security Access Guard
phase: Phase 3
last_updated: 2026-04-20
tags: [architecture, auth, workflow, security, hub-leaf]
---

# Canonical Auth Master Workflow

## Source of Truth
This document is the absolute architectural source of truth for auth routing. All internal logic in `:feature:auth` must follow these patterns.

## Current Implementation

### 1. Vendor Flow (Partner Side)
- **Sign Up**: Phone + OTP. If success, route to Category Selection (Onboarding). Save `AccountMode.PARTNER` hint.
- **Sign In**: Phone + OTP. Fetch `vendors/{uid}`.
  - If exists: Route to Dashboard.
  - If missing: Route to Category Selection.
  - Save `AccountMode.PARTNER` hint.

### 2. User Flow (Consumer Side)
- **Sign Up**: Phone + OTP. Create barebones `users/{uid}`. Save `AccountMode.USER` hint. Route to Decorators Gallery.
- **Sign In**: Phone + OTP. Fetch `users/{uid}`.
  - If exists: Route to Decorators Gallery.
  - If missing: Explicit failure (Account Not Found).
  - Save `AccountMode.USER` hint.

### 3. Session Restoration & Startup Resolution
Session restoration resolves returning users via `SplashViewModel` on cold launch. Detailed in [[session_restoration]].
- **Cold App Launch**: Starts at `SplashScreen` -> `resolveUserSession()`.
- **Identity Source**: `FirebaseAuth.currentUser`.
- **Role Hint**: `SessionStorage` (EncryptedSharedPreferences) stores `AccountMode`.
- **Resolution Path**:
  - If authenticated: Profile fetch from Firestore (`users` or `vendors` depending on hint).
  - If profile exists: Route terminal (`Dashboard` / `Gallery`).
  - If profile missing: Route to registration.
  - If unauthenticated: Route to `AuthGraph`.

### 4. Logout Consistency
Logout must clear both remote and local sessions.
1. `firebaseAuth.signOut()`
2. `sessionStorage.clearSession()` (Removes `AccountMode` hint)
3. Navigate to Login using `popUpTo(0)`.

### 5. Dev Bypass
The developer bypass flow replicates the Sign In flow exactly, including profile resolution and session hint storage. It is ONLY available in `DEBUG` builds.

## Database Role Constraints
- Roles are enforced via `firestore.rules`.
- `PARTNER` mode sessions cannot access `users/{uid}` data.
- `USER` mode sessions cannot access `vendors/{uid}` data.

---
[[Project_Hub|🏠 Project Hub]] | [[Auth_Hub|🔐 Auth Hub]] | [[auth_master_workflow|Top]]
