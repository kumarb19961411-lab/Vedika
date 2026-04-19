---
title: User Sign Up Implementation Guide
type: guide
status: active
owner: Android Compose Engineer
phase: Phase 3
last_updated: 2026-04-20
tags: [guide, auth, user, signup]
---

# User Sign Up Implementation Guide

## Source of truth
This guide covers the implementation details for the User Sign Up flow in Vedika, based on the [[auth_master_workflow]].

## Current implementation
1. **Component**: `PhoneAuthScreen` and `OtpVerificationScreen`.
2. **Logic**: The consumer enters their phone number. `AuthViewModel` triggers Firebase Phone Auth.
3. **Verification**: SMS OTP is received and verified.
4. **Registration**: The flow routes to **User Setup Node** (Ask Name) because the user does not exist in `users/{uid}`.
5. **Completion**: Upon submitting name, a document is written to `users/{uid}` and `popUpTo(0)` navigates to `Dashboard (Consumer)`.

## Future work
- Add welcome email/notification triggers upon successful signup.
