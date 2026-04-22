---
title: Android Build Regression Guard
type: guide
status: active
owner: All
phase: Phase 3
last_updated: '2026-04-22T00:00:00.000Z'
tags:
  - android
  - build
  - gradle
  - regression
  - guide
---
# 🛡️ Android Build Regression Guard

This guide provides a standardized "Pre-flight Build Checklist" and environment rules to maintain build stability in the Vedika repository.

## 🚀 Pre-flight Build Checklist
Before pushing code or finalizing a feature, ensure the following steps are completed:

1. **JDK Toolchain Check**:
   - Verify `gradle.properties` contains `org.gradle.java.home` pointing to the **Android Studio JBR** (JetBrains Runtime).
   - Rule: **Use Android Studio JBR** for this project to avoid `JdkImageInput` serialization errors.
2. **Clean State**:
   - If encountering "stale metadata" or weird Gradle transform errors, run:
     ```powershell
     ./gradlew clean
     # If issues persist
     rm -rf .gradle
     ```
3. **Repository Parity**:
   - If you changed an interface in `:core:data`, ensure the corresponding `Fake*Repository` in the same module is updated.
4. **Import & Package Hygiene**:
   - Verify every new `.kt` file has a correct `package` declaration matching its folder structure.
   - For `BuildConfig` usage in library modules, ensure you import the module-specific package (e.g., `com.example.vedika.feature.auth.BuildConfig`).
5. **UI Parameter Check**:
   - When refactoring to `hiltViewModel()`, ensure `NavHost` parameters in `MainActivity.kt` are updated to remove parameters now handled by `SavedStateHandle`.

## 🛠️ Environment Rules

### 1. JDK Pinning
We pin the JDK to the Android Studio JBR to ensure consistent code-generation and `jlink` behavior.
- **Location**: `gradle.properties`
- **Value**: `org.gradle.java.home=C:\\\\Program Files\\\\Android\\\\Android Studio\\\\jbr` (or equivalent path).

### 2. Dependency Resolution
- Never assume `Flow<T>` and `Result<T>` are interchangeable without explicit mapping.
- **Rule**: Use `.map { ... }` or `.catch { ... }` to translate repository results into UI states.

### 3. Smart Casting
- In `MainActivity.kt` or top-level navigation, always use explicit casting (e.g., `user as Authenticated`) within conditional branches to avoid \"Smart cast is impossible\" errors on mutable states.

---
[[Build_Troubleshooting_Guide|🛠️ Build Troubleshooting Guide]] | [[Project_Hub|🏠 Project Hub]] | [[SYSTEM_STATUS|📊 System Status]]
