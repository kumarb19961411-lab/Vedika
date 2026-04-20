---
title: Session Restoration Implementation Guide
type: guide
status: active
owner: Android Compose Engineer
phase: Phase 3
last_updated: 2026-04-20
tags: [guide, auth, session, restoration]
---

# Session Restoration Implementation Guide

## Overview
This guide describes how to maintain and verify the session restoration flow in the Vedika Android app.

## Components

### 1. `EncryptedSessionStorage`
- **Location**: `:core:data`
- **Role**: Stores the `AccountMode` enum securely using Android Keystore.
- **Key Method**: `saveAccountMode(AccountMode)`, `getAccountMode()`, `clearSession()`.

### 2. `SplashViewModel`
- **Location**: `:feature:auth`
- **Role**: State-driven startup resolver.
- **Logic**:
  1. Check `FirebaseAuth.currentUser`.
  2. If present, read `AccountMode` from `SessionStorage`.
  3. Fetch corresponding profile via `VendorRepository` or `UserRepository`.
  4. Emit `StartupState` (Authenticated, Unauthenticated, or Offline).

### 3. `MainActivity`
- **Role**: Top-level router.
- **Logic**: Observes `SplashViewModel.startupState` and navigates to `Dashboard`, `Gallery`, or `AuthGraph` accordingly.

## Verification Steps
1. **Cold Launch**: Close app, open it. Verify it lands on the last active screen (Dashboard or Gallery) without the Login screen flickering.
2. **Logout Verification**: Logout, close app, open it. Verify it lands on the Login screen (due to `clearSession()`).
3. **Offline Handling**: Disable internet, open app. Verify it shows the Offline/Retry screen. Do not auto-logout.

## Troubleshooting
- If the app consistently lands on Login even when logged in, verify that `AccountMode` is being saved correctly during the `verifyOtp` or `loginAsDevBypass` flow in `AuthViewModel`.
