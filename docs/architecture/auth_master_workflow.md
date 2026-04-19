# Canonical Auth Master Workflow

## Overview
This document defines the deterministic routing and state transitions for all User and Vendor authentication flows in Vedika. It supersedes all previous authentication notes.

## 1. Vendor Flow
- **Sign Up:** Vendor enters email/phone -> OTP -> Verified -> Navigates to Onboarding (Vendor Profile Bootstrap).
- **Sign In:** Vendor enters email/phone -> OTP -> Verified -> Checks backend for `vendors/{uid}`. If exists, route to Vendor Shell. If missing, route to Onboarding.

## 2. User Flow
- **Sign Up:** User enters phone -> OTP -> Verified -> Navigates to User Setup.
- **Sign In:** User enters phone -> OTP -> Verified -> Checks `users/{uid}`. If exists, route to User Shell. If missing, route to User Setup.

## 3. Session Restoration
- On app launch, `SplashViewModel` reads `EncryptedSharedPreferences` for valid token.
- If valid, bypass splash and route directly to Shell (Vendor or User).
- If expired/missing, route to Login Intro.

## 4. Role Resolution
- Roles are enforced via `firestore.rules`.
- Client caches role. Future phases will support user/vendor role toggling under single account.

## 5. Dev Bypass
- In `devDebug` (Emulator), entering `1234` immediately bypasses SMS verification.
