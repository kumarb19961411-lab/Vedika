# Phase 2 Runbook

## Environment Definitions
Vedika uses Build Flavors to dictate the injection of Data Sources.

### 1. Dev (Mock Mode)
- **What it is**: Complete offline mock application using `Fake*Repository` classes. No live Firebase backend.
- **How to run**: Select the `devDebug` build variant.
- **Command**: `./gradlew assembleDevDebug`
- **Limitations**: Data resets across app restarts. Cannot test true latency.

### 2. Staging (Test/Live Base)
- **What it is**: Uses Live Firebase Repositories (`FirebaseAuthRepository`, etc.) pointing to the staging Firebase backend.
- **Emulator Mode**: Can be hooked into local Emulators `10.0.2.2` via BuildConfig or specialized toggle. (Details WIP).
- **How to run**: Select the `stagingDebug` build variant.
- **Command**: `./gradlew assembleStagingDebug`

### 3. Prod (Live-Ready Mode)
- **What it is**: Uses Live Firebase Repositories pointed at the true production target. Strictly relies on live backend states, tightest security rules, and production databases.
- **How to run**: Select the `prodRelease` build variant.
- **Command**: `./gradlew assembleProdRelease`

## Troubleshooting
- **No Matching Client for Package Name**: If you receive a `google-services` build failure, ensure your `google-services.json` contains entries for the `.dev` and `.staging` suffixes respectively.
