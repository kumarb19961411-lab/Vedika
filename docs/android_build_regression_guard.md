# Android Build Regression Guard (Vedika V2)

This document serves as the source of truth for protecting the Vedika V2 Android build from regressions. All future agents and developers must verify compliance with these rules before submitting changes.

## 1. Protected Build Rules (Golden Path)

### Firebase & Package Identity
- **Rule**: Never use `applicationIdSuffix` in `dev` or `staging` flavors.
- **Reason**: Firebase `google-services.json` is configured for the base package `com.example.vedika`. Suffixes cause client-mismatch errors during initialization.
- **Current Config**: Checked and Verified in `app/build.gradle.kts`.

### Hilt & Code Generation
- **Rule**: `kotlin("kapt")` must be declared **before** `alias(libs.plugins.hilt.android)`.
- **Rule**: `correctErrorTypes = true` must be set in the `kapt` block of all modules using Hilt.
- **Reason**: Prevents "unresolved reference" errors during annotation processing and ensures clear error messages for Hilt dependency graph failures.

### Tooling & Compatibility
- **Rule**: Source and Target compatibility must be `JavaVersion.VERSION_17`.
- **Rule**: Kotlin `jvmTarget` must be `"17"`.
- **Reason**: Ensures compatibility with the latest Gradle JBR and Compose compiler metadata.

## 2. Shared State Architecture (Auth Workflow)

### Auth State Sharing
- **Rule**: The `AuthViewModel` must be shared across the `Login`, `SignUp`, and `OtpVerification` routes.
- **Implementation**: Obtain the instance once at the `NavHost` level in `MainActivity.kt` and pass it to the destination screens.
- **Reason**: Essential for preserving:
    - User input (Phone number) across screens.
    - Auth flow intent (`SIGN_IN` vs `SIGN_UP`).
    - Login mode (`USER` vs `VENDOR`).

## 3. Mandatory Dependency Verification

Before introducing new UI components, verify the following are included in the module's `build.gradle.kts`:
1. **Coil**: `libs.coil.compose` (for premium image loading/glassmorphism).
2. **Icons**: `libs.androidx.material.icons.extended` (for specialized UI symbols).
3. **Hilt Navigation**: `libs.androidx.hilt.navigation.compose` (for the `hiltViewModel()` utility).

## 4. Pre-Finalization Checklist
- [ ] No placeholder `clickable {}` or `onClick {}` handlers in production code.
- [ ] No hardcoded `errorMessage = "Error"` placeholders; use `uiState` mappings.
- [ ] All Compose imports are explicit (no unresolved `Icons` or `Alignment` references).

---
*Last Updated: 2026-04-10*
