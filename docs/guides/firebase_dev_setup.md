---
title: Firebase Development Setup
type: guide
status: active
owner: Firebase Backend & Data Model Engineer
phase: Phase 3
last_updated: 2026-04-22
tags: [firebase, dev, setup, emulator, appcheck, auth]
---

# 🔥 Firebase Development Setup

This guide provides the canonical steps to configure and verify Firebase services for development on Vedika, focusing on emulator stability and App Check protection.

## 🚀 1. Local Emulator Suite (devDebug)

The `devDebug` variant is pre-configured to route Auth and Firestore traffic to `10.0.2.2` (host loopback). 

> [!NOTE]
> For **Physical Devices**, you must ensure `USE_FIREBASE_EMULATOR` is `false` in your build config or use the `stagingDebug` variant to connect to live services.

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
1. Launch the app on your **emulator** or **physical device**.
2. Open **Logcat** and filter by `AppCheckDebugProvider`.
3. Locate the message: `Enter this debug secret into the allow list in the Firebase Console: <TOKEN>`.
4. Navigate to **Firebase Console > App Check > Apps**.
5. Select **Vedika (com.example.vedika)** > **Manage debug tokens**.
6. Add the token from Step 3.

> [!TIP]
> Registration is persistent for that specific device. If you clear app data or reinstall, you may need to re-register a new token.

## 📱 3. Physical Device Hardware Setup

To test on physical hardware, Firebase requires your machine's SHA fingerprints to be registered.

### Generating Fingeprints
Run the following in the project root:
```powershell
./gradlew signingReport
```

### Registration Steps
1. Copy the `SHA-1` and `SHA-256` keys from the terminal output.
2. Go to **Firebase Console > Project Settings > Your Apps**.
3. Select the Android App and click **Add Fingerprint**.
4. Paste the keys and save.
5. **Download the updated `google-services.json`** and replace the existing one in `Vedika/app/`.
6. **Verification**: I have successfully registered the `debug.keystore` prints for this environment—Phone Auth is functional.

## 📦 4. Firebase Storage Policy (Deferred)

As of April 2026, we have opted to **defer Firebase Storage** initialization to avoid "Blaze Plan" (Upgrade) requirements.

- **Current Implementation**: The app uses **Cloud Firestore** for all business data.
- **Image Strategy**: We use external CDN URLs (like Unsplash) for high-fidelity placeholders.
- **Future Revisit**: Storage will be enabled when user-originated media uploads (Gallery) become a priority in later phases.


## 🧪 5. Staging & Live Testing

### Standardized Vedika Test Numbers
| Number | Verification Code | Purpose |
| :--- | :--- | :--- |
| `+91 98888 77777` | `123456` | Vendor Registration Path |
| `+91 91111 22222` | `123456` | User Discovery Path |
| `+91 90000 00000` | `123456` | Troubleshooting/Admin |

> [!IMPORTANT]
> These numbers must be manually added to the **Firebase Console > Authentication > Settings > Phone numbers for testing**.

## 📦 6. Package & Configuration Integrity

### `google-services.json` Suffix Policy
You may notice `com.example.vedika.dev` and `com.example.vedika.staging` entries in `google-services.json`. 
- **Current Strategy**: To maintain match-stability and simplify Hilt/Dagger dependency injection, we currently use a **single package name (`com.example.vedika`)** across all flavors via the `flavorDimensions` configuration.
- **Future-Proofing**: The specific `.dev` and `.staging` entries are placeholders for potential future App Store/Play Store split listings. For now, they do not interfere with the active `com.example.vedika` client.

---
[[Firebase_Hub|🔥 Firebase Hub]] | [[backend_integration_blueprint|🧱 Backend Blueprint]] | [[android_build_regression_guard|🛡️ Build Guard]]
