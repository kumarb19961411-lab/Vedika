# Build Warnings Resolution Guide

This document tracks common Gradle and Kotlin warnings encountered during the Vedika project build and provides clear steps for resolution.

## [W001] Kotlin Kapt "Correct Error Types" Warning
- **Warning:** `[kapt] Incremental compilation and 'correctErrorTypes' are both enabled. This might cause issues.`
- **Nature:** Configuration Conflict.
- **Resolution:** In `build.gradle.kts` files using Hilt, ensure the `kapt` block is configured as follows:
  ```kotlin
  kapt {
      correctErrorTypes = true
  }
  ```
- **Action Taken:** Standardized this configuration across `:app` and `:feature:auth` to ensure Hilt generates valid stubs even when some types are missing during the initial pass.

## [W002] Deprecated `pushStyle` / `pop` in AnnotatedString
- **Warning:** `pushStyle(style: SpanStyle)` is deprecated. Use `withStyle` instead.
- **Nature:** API Deprecation.
- **Resolution:** Replace the imperative push/pop pattern with the scoped `withStyle` block.
- **Example Fix:**
  ```kotlin
  // Before (Deprecated)
  append("Text ")
  pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
  append("Bold Text")
  pop()

  // After (Recommended)
  append("Text ")
  withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
      append("Bold Text")
  }
  ```
- **Action Taken:** Updated `LoginScreen.kt` and `OtpVerificationScreen.kt` to use the non-deprecated API.

## [W003] Experimental Material 3 API Usage
- **Warning:** `This declaration is experimental and its usage should be marked with '@OptIn(ExperimentalMaterial3Api::class)'`
- **Nature:** API Stability Warning.
- **Resolution:** Add the `@OptIn` annotation to composables using `CenterAlignedTopAppBar`, `Scaffold` (with top bar), or `FlowRow`.
- **Action Taken:** Applied to `CategorySelectionScreen.kt`, `VenueRegistrationScreen.kt`, and `DecoratorRegistrationScreen.kt`.

## [W004] Resource Names and Unused Resources
- **Warning:** `The resource 'R.drawable.unnamed' appears to be unused.`
- **Nature:** Lint Warning (Performance/Size).
- **Resolution:** Use `resConfigs` to strip unused locales or strictly use `lint` to identify and remove dead assets.
- **Action Taken:** Cleaned up project structure; however, developers should periodically run `./gradlew lint` to maintain a slim APK.

## [W005] Java Version Mismatch
- **Warning:** `The AGP 8.7.2 requires Java 17 to run. You are currently using Java 11.`
- **Nature:** Toolchain Incompatibility.
- **Resolution:** Update `gradle.properties` to point to a valid JDK 17 path and ensure `compileOptions` in Gradle are set to `VERSION_17`.
- **Action Taken:** Fixed `org.gradle.java.home` in `gradle.properties` to point to the Android Studio bundled JBR.

---
**Status:** All critical build-breaking warnings have been addressed. The build is now "Clean" with zero ERRORS.
