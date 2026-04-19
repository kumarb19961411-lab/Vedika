---
title: Auth Master Workflow
type: workflow
status: active
owner: Product Architect
phase: 3
last_updated: 2026-04-15
tags: [auth, routing]
---

# 🔑 Auth Master Workflow

This document defines the deterministic routing and state transitions for the Vedika V2 authentication, onboarding flow, and session management.

## 🧭 The Canonical Auth Route Matrix

| Role Intent | Start Route | Authentication | Next Step (Profile Resolved) | Next Step (Profile Missing) | Final Destination |
|:---|:---|:---|:---|:---|:---|
| **Vendor** | `/signup` | Firebase OTP | (Not Possible) | Category Selection ➔ Setup | Vendor Dashboard |
| **Vendor** | `/login`  | Firebase OTP | Vendor Dashboard | (Error: Profile Required) | Vendor Dashboard |
| **User**   | `/signup` | Firebase OTP | (Not Possible) | User Setup Form | App Home |
| **User**   | `/login`  | Firebase OTP | App Home | (Error: Profile Required) | App Home |

## 1. Entering the App
- **Vendor/User App Boundary:** The UI will feature distinct entry points for Vendors (Partners) vs Users for Sign Up and Sign In.
- **Sign In Branch:** Routes to `/login`. Uses existing profile.
- **Sign Up Branch:** Routes to `/signup`. Mandates profile construction.

## 2. Authentication & OTP
All branches converge at OTP Verification:
- **Firebase Auth (Production/Staging):** Uses real SMS OTP dispatch.
- **Dev Mode / Emulator:** Use OTP `1234` for instant verification, bypassing real SMS boundaries. (Powered by Firebase Auth Emulator when active).

## 3. Session Restoration & Role Resolution
Upon opening the app (Relaunch):
1. **Rehydrate:** Interrogate Firebase Auth for the current JWT.
2. **Verify Claims/Profile:** Query Firestore `/users/{uid}` or `/vendors/{uid}` based on locally cached role enum.
3. **Restoration Outcome:**
    - If JWT valid & Profile exists ➔ Automatically route to Final Destination (Dashboard/Home).
    - If JWT valid & Profile missing ➔ Route to Setup Form (Recover broken onboarding).
    - If JWT invalid ➔ Drop to onboarding Welcome Screen.

## 4. Onboarding Completion Routing
Once a Profile Setup Form is submitted:
1. Validate inputs locally.
2. Commit full Profile document to Firestore (Bootstrap).
3. Clear the Auth Navigation Graph (`popUpTo(AuthGraph) { inclusive = true }`) to ensure the user cannot hit the "Back" button and return to OTP screens.
4. Route to Dashboard/Home.

## 5. Environment Behavior
- **Dev:** Uses Firebase Emulator Suite. `devDebug` connects to localhost:9099. Auth bypass enabled.
- **Staging:** Connects to live Firebase project (e.g., `vedika-staging`). Uses test phone numbers defined in Firebase console for automated verification checks.
- **Prod:** Full live SMS OTP limits enforced.
