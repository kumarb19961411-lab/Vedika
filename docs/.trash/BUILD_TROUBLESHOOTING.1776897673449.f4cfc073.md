# Build Troubleshooting and Resolution Guide

This document tracks significant build environment and compilation issues encountered during the development of Vedika and their respective fixes.

## 1. JDK Environment Issues

### Issue: `jlink` Error during Build
**Symptoms:**
Gradle build fails with an error related to `jlink` or missing modules, often pointing to a JRE instead of a JDK.
```
Execution failed for task ':app:extractIncludeDevDebugProto'.
> Could not find tools.jar. Please check that C:\Program Files\Java\jre1.8.0_291 contains a valid JDK installation.
```

**Cause:**
`org.gradle.java.home` in `gradle.properties` was pointing to a broken JRE (likely from a VS Code extension or a default system path) instead of the Android Studio JetBrains Runtime (JBR).

**Resolution:**
Update `gradle.properties` to point to a valid JDK or the JBR included with Android Studio.
```properties
org.gradle.java.home=C:\\Program Files\\Android\\Android Studio\\jbr
```

---

## 2. Multi-Module `BuildConfig` Issues

### Issue: Unresolved Reference 'BuildConfig' in Feature Modules
**Symptoms:**
Kotlin compilation fails in feature modules (e.g., `:feature:auth`) when trying to access `BuildConfig`.

**Cause:**
In multi-module projects, each module has its own `BuildConfig` class in its specific package. Accessing it requires an explicit import of the module-specific package (e.g., `import com.example.vedika.feature.auth.BuildConfig`) instead of the app's package.

**Constraint:**
Library modules do not have a `FLAVOR` field by default. Use `BuildConfig.DEBUG` for conditional logic in libraries.

---

## 3. Missing Package Declarations

### Issue: Unresolved Reference for Local Classes
**Symptoms:**
A Kotlin file cannot find ViewModels, Data Models, or other classes located in the same directory, despite them being physically present.

**Cause:**
The file was missing the `package` declaration at the top (e.g., `package com.example.vedika.feature.discovery`). Without this, the compiler treats the file as being in the default (unnamed) package, making it unable to "see" other classes in the module.

---

## 4. Compose API Updates

### Issue: `FlowRow` Migration
**Symptoms:**
`FlowRow` from `com.google.accompanist.flowlayout` is deprecated or unavailable.

**Resolution:**
Use the standard `androidx.compose.foundation.layout.FlowRow`. This requires the `@ExperimentalLayoutApi` annotation.

```kotlin
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyComposable() {
    FlowRow { /* ... */ }
}
```

---

## 5. Navigation Parameter Mismatches

### Issue: `No parameter with name '...' found`
**Symptoms:**
`MainActivity.kt` fails to compile when calling a screen composable within `NavHost`.

**Cause:**
Mismatch between the parameters expected by the screen Composable (e.g., `VendorDetailScreen`) and the arguments passed in `NavHost`. This often happens when refactoring to use `hiltViewModel()` which handles `SavedStateHandle` (and thus navigation arguments) internally, removing the need to pass them explicitly as parameters.
