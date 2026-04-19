---
title: Vendor Sign In Implementation Guide
type: guide
status: active
owner: Android Compose Engineer
phase: Phase 3
last_updated: 2026-04-20
tags: [guide, auth, vendor, signin]
---

# Vendor Sign In Implementation Guide

## Source of truth
This guide covers the implementation details for the Vendor Sign In flow in Vedika, based on the [[auth_master_workflow]].

## Current implementation
The Vendor Sign In flow utilizes Jetpack Compose and `AuthViewModel`.
1. **Component**: `PhoneAuthScreen` and `OtpVerificationScreen`.
2. **Logic**: The user enters their phone number. `AuthViewModel` triggers Firebase Phone Auth.
3. **Verification**: SMS OTP is received and verified.
4. **Validation**: The backend checks `vendors/{uid}`.
5. **Onboarding Check**: The system asserts `onboardingComplete` flag is true. If false, it reroutes to the Vendor Onboarding flow.
6. **Completion**: If true, `popUpTo(0)` navigates the user to the `Dashboard (Partner Home)`.

See `feature:auth` module for the active implementation.

## Future work
- Refine error handling when network is disrupted during the verify stage.
