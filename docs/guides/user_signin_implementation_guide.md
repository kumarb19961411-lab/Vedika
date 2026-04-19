---
title: User Sign In Implementation Guide
type: guide
status: active
owner: Android Compose Engineer
phase: Phase 3
last_updated: 2026-04-20
tags: [guide, auth, user, signin]
---

# User Sign In Implementation Guide

## Source of truth
This guide covers the implementation details for the User Sign In flow in Vedika, based on the [[auth_master_workflow]].

## Current implementation
1. **Component**: `PhoneAuthScreen` and `OtpVerificationScreen`.
2. **Logic**: The consumer enters their phone number to log in. `AuthViewModel` triggers Firebase Phone Auth.
3. **Verification**: SMS OTP is received and verified.
4. **Validation**: The backend checks `users/{uid}`.
5. **Completion**: If document exists, `popUpTo(0)` navigates immediately to `Dashboard (Consumer)`.

## Future work
- Standardize error messages if the user attempts to sign in but has no existing account (prompt to sign up).
