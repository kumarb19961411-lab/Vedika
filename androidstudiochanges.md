# Android Studio Changes Log

## [1.0.0] - 2025-05-22

### Issue 1: Firebase Package Name Mismatch
- **Error Faced:** `Execution failed for task ':app:processDevDebugGoogleServices'. > No matching client found for package name 'com.example.vedika.dev' in C:\Users\Welcome\Documents\GitHub\Vedika\app\google-services.json`
- **Cause:** The `dev` and `staging` product flavors in `app/build.gradle.kts` were using `applicationIdSuffix`, resulting in package names (`com.example.vedika.dev`) that weren't defined in the `google-services.json` file.
- **Mitigation:** Removed the `applicationIdSuffix` from the `productFlavors` configuration.
- **Why:** To align the app's package name with the existing Firebase configuration. This allows the `google-services` plugin to find a matching client and successfully process the configuration file.
- **Code Fix in `app/build.gradle.kts`:**
  ```kotlin
  productFlavors {
      create("dev") {
          dimension = "environment"
          // Removed: applicationIdSuffix = ".dev"
          versionNameSuffix = "-dev"
      }
      create("staging") {
          dimension = "environment"
          // Removed: applicationIdSuffix = ".staging"
          versionNameSuffix = "-staging"
      }
      // ...
  }
  ```

### Issue 2: Invalid Gradle Java Home
- **Error Faced:** `Value 'C:\Program Files\Eclipse Adoptium\jdk-17.0.8.7-hotspot' given for org.gradle.java.home Gradle property is invalid (Java home supplied is invalid)`
- **Cause:** The `gradle.properties` file contained a hardcoded path to a JDK that did not exist on the local machine.
- **Mitigation:** Corrected the path to use the Android Studio bundled JDK (JBR).
- **Why:** Ensuring the build uses a valid JDK path is essential for Gradle to run.
- **Code Fix in `gradle.properties`:**
  ```properties
  # Pin Gradle to the system JDK 17 — prevents VS Code extension JRE from being used
  org.gradle.java.home=C\:\\\\Program Files\\\\Android\\\\Android Studio\\\\jbr
  ```

### Issue 3: Hilt/Kotlin Metadata Version Mismatch
- **Error Faced:** `[Hilt] Provided Metadata instance has version 2.2.0, while maximum supported version is 2.1.0.`
- **Cause:** Incompatibility between Kotlin 2.1+ and older Hilt versions (2.51.1).
- **Mitigation:** Upgraded Hilt to version 2.59.2 and corrected the compiler artifact name.
- **Why:** Newer versions of Hilt are required to support the metadata format produced by recent Kotlin versions.
- **Code Fix in `libs.versions.toml`:**
  ```toml
  hilt = "2.59.2"
  hilt-compiler = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "hilt" }
  ```

### Project Upgrade
The upgraded project successfully synced with the IDE. You should test that the upgraded project builds and passes its tests successfully before making further changes.
The upgrade consisted of the following steps:
- Upgrade AGP dependency from 8.13.2 to 9.1.0
- Upgrade Gradle version to 9.3.1
- Upgrade Gradle plugins
- Enable resValues build feature
- Disable targetSdk defaults to compileSdk
- Disable App Compile-Time R Class
- Continue to allow `intent-filters` in the main manifest
- Allow non-unique package names
- Enable Dependency Constraints
- Disable R8 Strict Mode for Keep Rules
- Disable R8 Optimized Resource Shrinking
- Disable built-in Kotlin support
- Preserve the old (internal) AGP Dsl APIs
