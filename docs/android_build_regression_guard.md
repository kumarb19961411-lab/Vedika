# Android Build Regression Guard — Mandatory Stability Rules

This document contains strictly enforced build and configuration policies for Vedika V2. No PR or change shall be accepted that violates these stability requirements.

## 1. Firebase & Identity Guard (Golden Path)
- **Constraint**: Never use `applicationIdSuffix` in any product flavor.
- **Why**: `google-services.json` is hardcoded to `com.example.vedika`. Suffixes like `.dev` break client resolution during build.
- **Reference**: `androidstudiochanges.md` (Issue 1).

## 2. Hilt & Build Environment Guard
- **Toolchain**: AGP `8.7.2` + Kotlin `2.0.21` is the current stable baseline.
- **Compiler**: Hilt must be pinned to `2.52` or later for Kotlin 2.0+ compatibility.
- **Order of Operations**: In every `build.gradle.kts`, `kotlin("kapt")` **must** be applied before `libs.plugins.hilt.android`.
- **Error Types**: The `kapt` block must contain `correctErrorTypes = true`.

## 3. Toolchain & Standard Guard
- **Java Version**: Always use **Java 17**.
- **Settings**: 
    - `gradle.properties`: `org.gradle.java.home` should point to JDK 17.
    - `compileOptions`: `sourceCompatibility` and `targetCompatibility` = `JavaVersion.VERSION_17`.
    - `kotlinOptions`: `jvmTarget = "17"`.

## 4. Navigation & State Guard
- **Shared ViewModel**: Access `AuthViewModel` using `hiltViewModel(parentEntry)` where `parentEntry` is retrieved via `navController.getBackStackEntry(VedikaDestination.AuthGraph.route)`.
- **Scoping**: This ensures state (Flow, Mode, Form Data) survives transitions within the `auth_graph`.
- **Handoff**: Registration completion must use `popUpTo(AuthGraph) { inclusive = true }`.

## 5. UI Library & Dependency Guard
- **Coil**: When using `AsyncImage`, ensure `libs.coil.compose` is in the module dependency list.
- **Material Icons**: Use `libs.androidx.material.icons.extended` for additional UX indicators.

## 6. Data Continuity Guard
- **Mock Authority**: `core:data:VendorRepository` is the singleton source of truth for mock vendor data during Phase 2.
- **Contract**: Any new field entered during registration MUST be added to `VendorMockState` and mapped in `AuthViewModel` to ensure it reflects on the Dashboard.
- **OTP Logic**: All authentication bypasses must use a standardized **6-digit** code: `123456`. The UI must provide 6 distinct input slots (sized appropriately for mobile displays, e.g., 48dp width) and trigger auto-verification on the 6th digit. Stale 4-digit logic is prohibited.

---
*Policy violations will lead to build breakage. Re-verify after any dependency update.*
