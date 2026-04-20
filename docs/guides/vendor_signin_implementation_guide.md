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
This guide covers the deep implementation details for the Vendor Sign In component lifecycle in Vedika. It specifically translates the architecture defined in the [[auth_master_workflow]] document into actionable execution logic utilizing Android Jetpack Compose constructs alongside standard `ViewModel` boundaries interacting specifically with mocked repository patterns transitioning toward Firebase live sources.

## Current implementation

### 1. View & Interaction Layer
- Main Screen Component: `VendorLoginScreen.kt` maps explicit layout logic capturing valid Phone Number parameters through a highly customized Material 3 TextField implementation structured with explicit composable state hoisting.
- User submits formatted digits explicitly routing input payloads back toward highly normalized `AuthViewModel.authenticateVendor()` methods.

### 2. ViewModel & Emulation Interception
- `AuthViewModel` captures incoming state parameters establishing standard internal `AuthUiState.Loading` emissions immediately locking out standard user interaction arrays via overlay mechanisms.
- Rather than strictly bounding towards production Firebase SMS invocations currently, it leverages controlled bypassing functions. Providing numeric inputs strictly matching `"123456"` executes highly predictable state intercepts forcefully triggering simulated OTP resolution protocols instantly verifying intent natively ensuring unblocked QA automations.

### 3. Verification & Routing Resolution
- Successful simulated resolution internally shifts operational contexts explicitly executing cross-module function queries directly targeting underlying active implementations of `VendorRepository.verifyVendorExists()`.
- Resolving explicitly identified constraints asserting deep property validation specifically checking if standard `onboardingComplete == true` conditions uniquely authorize complete transitions.
- Fully bounded resolutions execute explicit `NavHostController.navigate("vendorDashboard")` actions actively utilizing complex back-stack clearance flags formally defined as `popUpTo("auth_graph") { inclusive = true }`.

### 4. Failed Validations
- Operations attempting to leverage credentials mapping firmly against `vendors/{uid}` collections containing an explicit missing or statically `false` property within standard `onboardingComplete` definitions securely bounce routing requests mapping directly into specific Onboarding forms implicitly based exclusively upon previously instantiated contextual data inputs accurately.

## Future work
- Aggressively harden network disruption handling explicitly during core `verifyVendorExists()` operations yielding explicit user-facing Snackbar elements handling standard exceptions cleanly.

---
[[Project_Hub|🏠 Project Hub]] | [[Vendor_Flow_Hub|🏢 Vendor Flow Hub]] | [[vendor_signin_implementation_guide|Top]]
