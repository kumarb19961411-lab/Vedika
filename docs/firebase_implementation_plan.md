# Firebase Integration (Phase 2)

We are ready to start **Phase 2: Firebase Integration** for the Vedika Vendor app. This step connects our Phase 1 UI/Architecture to real data on Firebase using your project: `vedika-e44be` (799731847794).

## User Review Required

> [!IMPORTANT]  
> Please review the steps below. Once you approve, I will automatically execute them.

## Proposed Changes

### 1. Register App & Configuration
- Use the Firebase MCP to register an Android app (`com.example.vedika`) inside the `vedika-e44be` project.
- Retrieve the `google-services.json` configuration and place it in the `app/` module.
- Add the `vedika-e44be` project alias to `.firebaserc`.

### 2. Gradle Dependencies Updates
- **Project `build.gradle.kts`**: Add `com.google.gms:google-services` class path.
- **Module `app/build.gradle.kts`**: Apply the `com.google.gms.google-services` plugin.
- **`libs.versions.toml`**: Add Firebase BoM, `firebase-auth`, and `firebase-firestore`. Add them to the `:core:data` and `:app` dependencies.

### 3. Firebase DI Setup (Hilt)
- Keep our mocked repositories running in the `dev` flavor by maintaining `DevRepositoryModule` in `app/src/dev/java`.
- Create a new `FirebaseRepositoryModule` in `app/src/main/java` which will be used by `staging` and `prod` flavors. This ensures we can still test UI without making network calls in `dev`.

### 4. Implement Real Repositories (`:core:data`)
We will create actual Firebase classes in `:core:data` to start replacing our mocks:
- **`FirebaseAuthRepository`**: Implement Phone-based OTP authentication logic.
- **`FirebaseVendorRepository`**: Firestore integration for storing/getting the business and vendor profiles.
- **`FirebaseBookingRepository`**: Firestore integration for fetching/updating/creating calendar bookings.
- **`FirebaseInventoryRepository`**: Firestore integration for inventory items.

### 5. Firestore Pre-requisites
- Use `firebase_init` to define core Firestore rules and setup the local workspace for deployments so security rules and indexes can be applied later once data structures are solidified.

## Open Questions

> [!WARNING]  
> Before we start hooking up Phone Authentication, we need to ensure that **Phone Auth is enabled manually in the Firebase Console**. I cannot enable this via CLI/API. Please confirm if you have enabled Phone Auth in your Firebase Console for `vedika-e44be` or if you will do it during testing.

## Verification Plan
1. **Build Success**: The app compiles cleanly on the `stagingDebug` variant (which uses the new Firebase dependencies).
2. **App Startup**: The app can boot and initialize `FirebaseApp` without crashing due to missing JSON Configs.
