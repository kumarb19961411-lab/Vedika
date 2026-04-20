---
title: Android Build Regression Guard
type: guide
status: active
owner: All
phase: Phase 3
last_updated: 2026-04-21
tags: [android, build, gradle, regression, guide]
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
4. **Import Hygiene**:
   - Scan for "Unresolved reference" in ViewModels. Common missing imports: `AuthRepository`, `VendorRepository`.
5. **UI Parameter Check**:
   - Ensure `Icon` size is passed via `Modifier.size()` instead of direct parameters if using standard Material 3 wrappers.

## 🛠️ Environment Rules

### 1. JDK Pinning
We pin the JDK to the Android Studio JBR to ensure consistent code-generation and `jlink` behavior.
- **Location**: `gradle.properties`
- **Value**: `org.gradle.java.home=C:\\Program Files\\Android\\Android Studio\\jbr` (or equivalent path).

### 2. Dependency Resolution
- Never assume `Flow<T>` and `Result<T>` are interchangeable without explicit mapping.
- **Rule**: Use `.map { ... }` or `.catch { ... }` to translate repository results into UI states.

### 3. Smart Casting
- In `MainActivity.kt` or top-level navigation, always use explicit casting (e.g., `user as Authenticated`) within conditional branches to avoid "Smart cast is impossible" errors on mutable states.

---
[[Project_Hub|🏠 Project Hub]] | [[SYSTEM_STATUS|📊 System Status]] | [[build_stabilization_lessons|🎓 Stabilization Lessons]]
