---
title: Firebase Development Setup
type: guide
status: active
owner: Firebase Backend & Data Model Engineer
phase: Phase 3
last_updated: 2026-04-21
tags: [firebase, dev, setup, emulator, appcheck, auth]
---

# 🔥 Firebase Development Setup

This guide provides the canonical steps to configure and verify Firebase services for development on Vedika, focusing on emulator stability and App Check protection.

## 🚀 1. Local Emulator Suite (devDebug)

The `devDebug` variant is pre-configured to route all Auth and Firestore traffic to `10.0.2.2` (the Android Studio host loopback).

### Prerequisites
- Install the [Firebase CLI](https://firebase.google.com/docs/cli).
- Run the emulator suite from the repository root:
  ```powershell
  firebase emulators:start --import=./emulator_data
  ```

### Authentication (Bypass)
- The app currently implements an **OTP Dev Bypass** for the `devDebug` variant.
- Any valid-looking number can be used; the emulator will not send real SMS.
- Standardized Test Number: `+91 99999 99999` | Code: `123456`

## 🛡️ 2. App Check Hardening

We use the **Firebase App Check Debug Provider** to allow the emulator to interact with live Firebase services (if needed) and to simulate a hardened production environment.

### Registering your Debug Token
1. Launch the app in the emulator.
2. Open **Logcat** and filter by `DebugAppCheckProvider`.
3. Locate the message: `Enter this debug secret into the allow list in the Firebase Console: <TOKEN>`.
4. Navigate to **Firebase Console > Security > App Check > Apps**.
5. Select **Vedika (com.example.vedika)** > **Manage debug tokens**.
6. Add the token from Step 3.

> [!WARNING]
> Never commit a debug token to the repository. The `DebugAppCheckProvider` should only exist in `debug` source sets or behind `BuildConfig.DEBUG` flags.

## 📱 3. Staging & Live Testing

For variants pointing to live Firebase (`stagingDebug`, `prodRelease`), use **Fictional Phone Numbers** to avoid SMS costs and rate limits.

### Standardized Vedika Test Numbers
| Number | Verification Code | Purpose |
| :--- | :--- | :--- |
| `+91 98888 77777` | `123456` | Vendor Registration Path |
| `+91 91111 22222` | `123456` | User Discovery Path |
| `+91 90000 00000` | `123456` | Troubleshooting/Admin |

> [!IMPORTANT]
> These numbers must be manually added to the **Firebase Console > Authentication > Settings > Phone numbers for testing**.

## 📦 4. Package & Configuration Integrity

### `google-services.json` Suffix Policy
You may notice `com.example.vedika.dev` and `com.example.vedika.staging` entries in `google-services.json`. 
- **Current Strategy**: To maintain match-stability and simplify Hilt/Dagger dependency injection, we currently use a **single package name (`com.example.vedika`)** across all flavors via the `flavorDimensions` configuration.
- **Future-Proofing**: The specific `.dev` and `.staging` entries are placeholders for potential future App Store/Play Store split listings. For now, they do not interfere with the active `com.example.vedika` client.

---
[[Firebase_Hub|🔥 Firebase Hub]] | [[backend_integration_blueprint|🧱 Backend Blueprint]] | [[android_build_regression_guard|🛡️ Build Guard]]
