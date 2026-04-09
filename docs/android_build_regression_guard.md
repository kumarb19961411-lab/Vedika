# Android Build Regression Guard

This document serves as the protective guardrail for the Vedika V2 Android project. It contains mandatory rules that must be followed by all future agents and developers to prevent recurring build failures and Firebase regressions.

## 1. Golden Path: Single Package Name
- **Rule**: `applicationIdSuffix` MUST NOT be used in any product flavors for Vedika V2.
- **Why**: To ensure 100% compatibility with the `google-services.json` contract. Reintroducing suffixes breaks Firebase Auth, Firestore, and Analytics client matching.
- **Status**: Enforced. Current `applicationId` for all environments is `com.example.vedika`.

## 2. Java Stability
- **Rule**: Java 17 is the mandatory source and target compatibility standard for the entire project.
- **IDE Property**: `org.gradle.java.home` in `gradle.properties` should point to a valid JDK 17 (preferably the Android Studio `jbr` path).
- **Check**: Verify `compileOptions` and `kotlinOptions` are set to `17` in all new modules.

## 3. Hilt & Kapt Dependency Chain
- **Rule**: In all `plugins` blocks, `kotlin("kapt")` MUST be applied BEFORE `alias(libs.plugins.hilt.android)`.
- **Annotation Processing**: `kapt { correctErrorTypes = true }` MUST be present in all modules using Hilt.
- **Stable Versions**: Do not upgrade Hilt, Kotlin, or AGP beyond the current stable baseline without explicit regression testing for Hilt metadata compatibility.

## 4. UI Module Dependencies
- **Rule**: All feature/UI modules using `AsyncImage` or network images MUST include `libs.coil.compose`.
- **Imports**: Avoid using deprecated Compose APIs (e.g., use `fontStyle = FontStyle.Italic` instead of older parameters).

## Build Safety Checklist (Pre-Merge)
- [ ] No `applicationIdSuffix` present in `app/build.gradle.kts`.
- [ ] `kapt` applied before `hilt` in all modified modules.
- [ ] `org.gradle.java.home` exists and points to JDK 17.
- [ ] Module `namespace` follows `com.example.vedika` prefix.
- [ ] No machine-specific hardcoded paths added to Gradle files.
- [ ] Successful configuration check: `./gradlew help` (no JVM or plugin errors).

## Historical Issues Resolved (Source of Truth)
Refer to [androidstudiochanges.md](file:///c:/Users/Welcome/Documents/GitHub/Vedika/androidstudiochanges.md) for the full history of the five core build issues resolved during the early V2 transition.
