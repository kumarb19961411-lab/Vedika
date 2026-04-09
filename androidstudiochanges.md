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

### Issue 4: Hilt Annotation Processor Error
- **Error Faced:** `[Hilt] Expected @AndroidEntryPoint to have a value. Did you forget to apply the Gradle Plugin? (com.google.dagger.hilt.android)`
- **Cause:** 
    - **Plugin Order:** In several modules, the Hilt Gradle plugin was applied before the Kotlin Kapt plugin. Hilt requires Kapt to be initialized first to correctly inject annotation processor arguments.
    - **Java Version:** The project was using Java 11, but the latest AGP and Kotlin versions require Java 17 for stability and compatibility.
    - **Tooling Instability:** The experimental AGP 9.1.0 and Kotlin 2.2.10 versions were causing issues with Hilt's stub generation.
- **Mitigation:**
    - Reordered plugins in all `build.gradle.kts` files to ensure `kotlin("kapt")` is applied before `alias(libs.plugins.hilt.android)`.
    - Updated `compileOptions` and `kotlinOptions` to use **Java 17**.
    - Standardized the project on stable tooling versions: AGP **8.7.2**, Kotlin **2.0.21**, and Hilt **2.51.1**.
    - Added `correctErrorTypes = true` to the `kapt` configuration block.
- **Why:** Ensures Hilt can correctly process annotations like `@AndroidEntryPoint` and `@HiltAndroidApp` during the Kapt task execution.
- **Code Fix in `app/build.gradle.kts` (and others):**
  ```kotlin
  plugins {
      // ...
      kotlin("kapt") // Must come before Hilt
      alias(libs.plugins.hilt.android)
  }

  android {
      compileOptions {
          sourceCompatibility = JavaVersion.VERSION_17
          targetCompatibility = JavaVersion.VERSION_17
      }
      kotlinOptions {
          jvmTarget = "17"
      }
  }

  kapt {
      correctErrorTypes = true
  }
  ```

### Issue 5: Unresolved References and Missing Imports in UI Modules
- **Error Faced:** Multiple `Unresolved reference` errors in `feature:auth` module screens (e.g., `BorderStroke`, `FontStyle`, `linearGradient`, `FilterVintage`).
- **Cause:** 
    - Missing explicit imports for standard Compose Foundation and UI components.
    - Usage of deprecated or incorrect parameters in `Text` and `buildAnnotatedString` (e.g., `italic = true` instead of `fontStyle = FontStyle.Italic`).
    - Missing `coil-compose` dependency in the feature module.
- **Mitigation:**
    - Added `implementation(libs.coil.compose)` to `feature/auth/build.gradle.kts`.
    - Added explicit imports for `androidx.compose.foundation.BorderStroke`, `androidx.compose.ui.text.font.FontStyle`, `androidx.compose.ui.graphics.Brush`, and others.
    - Updated `buildAnnotatedString` blocks to use the modern `withStyle` API:
      ```kotlin
      // Before
      pushStyle(SpanStyle(...))
      append("Text")
      pop()

      // After
      withStyle(SpanStyle(...)) {
          append("Text")
      }
      ```
    - Replaced `italic = true` with `fontStyle = FontStyle.Italic` in `Text` composables.
    - Fixed missing icon references (e.g., added `import androidx.compose.material.icons.filled.FilterVintage`).
- **Why:** To align with Jetpack Compose best practices and ensure all UI components and icons are correctly resolved during compilation.

### Project Build Status
The project now builds successfully using `:app:assembleDevDebug`.
