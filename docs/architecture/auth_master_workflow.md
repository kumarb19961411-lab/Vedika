# Canonical Auth Master Workflow

## Overview
This document serves as the absolute source of truth defining the deterministic routing, database interactions, and state transitions for all User and Vendor authentication flows in Vedika. All feature modules interacting with `feature:auth` must abide by these patterns to prevent session memory leaks.

## 1. Vendor Flow (Partner)
Vendor accounts strictly require full profile completions before triggering terminal graph clearance.
- **Sign Up:** Vendor enters phone number -> Receives OTP -> Verifies OTP -> Caches `uid` locally -> Navigates to Onboarding Flow (Vendor Profile Bootstrap) -> Onboarding Completed -> `vendors/{uid}` Document Written -> `popUpTo(0)` Dashboard.
- **Sign In:** Vendor enters phone number -> Receives OTP -> Verifies OTP -> `AuthViewModel` queries `vendors/{uid}` -> Document exists? -> Assert `onboardingComplete` flag is true -> `popUpTo(0)` Dashboard. If false or missing, force route back to Onboarding Flow.

## 2. User Flow (Consumer)
User accounts prioritize speed and frictionless entry over deep data completion.
- **Sign Up:** User enters phone number -> OTP -> Verified -> Navigates to User Setup Component (Ask Name) -> Submits -> `users/{uid}` Document Written -> `popUpTo(0)` Dashboard.
- **Sign In:** User enters phone number -> OTP -> Verified -> Query `users/{uid}` -> Document exists? -> `popUpTo(0)` Dashboard.

## 3. Session Restoration & Handshake
- On cold app launch, `SplashViewModel` reads `EncryptedSharedPreferences` for a valid, non-expired authentication token.
- **Hit:** If token is valid and role is identifiable, bypass splashing, instantiate required backend streams, and route directly to the appropriate Shell.
- **Miss:** If token is expired or completely missing, wipe shared preferences block and instantiate the introductory Login/Registration splash screen.

## 4. Database Role Enforcement
- Roles are purely structural and enforced via `firestore.rules`. Meaning, a client app UI state `AccountMode == PARTNER` cannot write to the `users/{uid}` collection if the token belongs to a different authorization tier.
- Client caches role on login. Later phases will explore toggling scopes if one person acts as both a user and a vendor.

## 5. Development Utilities
- **Dev Bypass**: When compiling `devDebug` pointing to the internal Firebase Emulator, entering the magic number `1234` inside the OTP Verification flow immediately bypasses SMS transit simulation and authenticates the flow securely for rapid QA testing.
