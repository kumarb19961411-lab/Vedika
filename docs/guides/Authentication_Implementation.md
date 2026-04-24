---
last_updated: '2026-04-24T00:00:00.000Z'
phase: Phase 4 - Stabilization
status: active
tags:
  - guide
  - auth
  - implementation
title: Authentication Implementation Guide
---
# 🔐 Authentication Implementation Guide

This document is the authoritative reference for Vedika's authentication flows, covering sign-in and sign-up for both Consumers and Vendors.

---

## 👤 Consumer Authentication

### 1. User Sign-In
- **View Layer**: `UserLoginScreen.kt` manages phone number input with strictly typed components and validation.
- **Logic**: `AuthViewModel.authenticateUser()` handles the submission, mutating UI state to `Loading` during the process.
- **Bypass Mode**: For local testing, use the code `123456` to bypass SMS gateway logic.
- **Routing**: Successful verification checks for existing data in `users/{uid}`. If absent, it redirects to the User Signup flow; otherwise, it navigates to the Dashboard.

### 2. User Sign-Up
- **View Layer**: `UserSignupScreen.kt` captures user details (Name) after initial OTP verification.
- **Data Persistence**: Creates a new DTO in `users/{uid}` with base roles.
- **Completion**: Executes `popUpTo("auth_graph") { inclusive = true }` to land on the Consumer Dashboard.

---

## 🏢 Vendor Authentication

### 1. Vendor Sign-In
- **View Layer**: `VendorLoginScreen.kt` captures phone numbers using Material 3 TextField with state hoisting.
- **ViewModel**: `AuthViewModel.authenticateVendor()` manages the state transitions.
- **Verification**: Checks if `onboardingComplete == true` in the `vendors/{uid}` collection.
- **Failed Validation**: If onboarding is incomplete, the user is redirected to the Vendor Onboarding/Registration flow.

### 2. Vendor Registration/Onboarding
- **Flow**: Managed via `auth_graph`. Redirects to business profile setup after OTP verification.
- **Backstack Management**: Upon completion, the registration flow is purged from the backstack to prevent re-entry.

---

## 🛠️ Technical Commonality

### Verification Sequence Hooks
- All authentication parameters trigger internal validation functions reading target node representations (`users/{uid}` or `vendors/{uid}`).
- Navigation routines consistently use `popUpTo("auth_graph") { inclusive = true }` for clean entry into the shell.

### Future Hardening
- Implement robust network disruption handling during `verifyVendorExists()` operations.
- Integrate "Welcome" notifications and introductory system metrics upon successful account creation.

---
[[Project_Hub|🏠 Project Hub]] | [[Auth_Hub|🔐 Auth Hub]]
