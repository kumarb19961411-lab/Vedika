---
title: Session Restoration Strategy
type: architecture
status: active
owner: Security Access Guard
phase: Phase 3
last_updated: 2026-04-20
tags: [architecture, auth, session, security, hub-leaf]
---

# Session Restoration Strategy

## Source of Truth
The canonical source of truth for user identity is **Firebase Auth**. The application resolves session state on startup to provide a seamless "zero-landing" experience for returning users.

## Current Implementation

### 1. Persistence Layer (`EncryptedSessionStorage`)
We use `EncryptedSharedPreferences` to store non-sensitive **startup hints**. This allows the app to know which profile repository (User vs. Vendor) to query before the full UI is rendered.
- **Stored Data**:
  1. `AccountMode`: Enum (`USER` or `PARTNER`) designating the last active context.
  
> [!NOTE]
> No sensitive PII (names, phone numbers) or Auth Tokens are stored manually. Firebase handles token persistence and rotation internally.

### 2. Startup Lifecycle (`SplashViewModel`)
1. **App Launch**: `MainActivity` sets `SplashScreen` as the `startDestination`.
2. **Session Resolution**: `SplashViewModel` checks `FirebaseAuth.currentUser`.
   - **If Null**: Emits `StartupState.Unauthenticated` -> Routes to `AuthGraph`.
   - **If Authenticated**: Reads `AccountMode` hint from `SessionStorage`.
3. **Profile Validation**:
   - The app fetches the corresponding profile from Firestore (`users/{uid}` or `vendors/{uid}`).
   - If profile exists: Routes to terminal screen (`Dashboard` or `DecoratorsGallery`).
   - If profile missing: Routes to registration/setup (`CategorySelection`).
4. **Offline Resilience**: If the network lookup fails during startup, the app shows an Offline/Retry state rather than logging the user out prematurely.

### 3. Session Termination
On logout, the following must occur atomically:
1. `FirebaseAuth.signOut()`
2. `SessionStorage.clearSession()` (Purges the `AccountMode` hint)
3. Navigation backstack is purged (`popUpTo(0)`) to the Login screen.

## Future Work
- Add proactive session validation alerts if the underlying Firebase token becomes unrecoverable.

---
[[Project_Hub|🏠 Project Hub]] | [[Auth_Hub|🔐 Auth Hub]] | [[session_restoration|Top]]
