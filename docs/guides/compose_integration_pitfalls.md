---
title: Compose Integration Pitfalls
type: guide
status: active
owner: All
phase: Phase 3
last_updated: 2026-04-21
tags: [android, compose, ui, pitfalls, guide]
---

# 🎨 Compose Integration Pitfalls

This guide captures common "gotchas" encountered during the Milestone 1 UI/UX polish and stabilization pass.

## ⚠️ Common Pitfalls

### 1. `Icon` Parameter Sprawl
- **The Issue**: Trying to set `size` or `padding` directly on an `Icon` component.
  ```kotlin
  // ❌ WRONG
  Icon(imageVector = Icons.Default.Add, size = 24.dp) 
  ```
- **The Fix**: Standard Material 3 `Icon` components do not have a `size` parameter. Use `Modifier`.
  ```kotlin
  // ✅ CORRECT
  Icon(
      imageVector = Icons.Default.Add,
      modifier = Modifier.size(24.dp),
      contentDescription = null
  )
  ```

### 2. State-Loss on Navigation Recomposition
- **The Issue**: Instantiating a `ViewModel` inside a composable instead of using Hilt injection or navigation-scoped owners.
- **The Fix**: Use `hiltViewModel()` within the composable to ensure the lifecycle is correctly tied to the NavBackStackEntry.

### 3. Argument Type Mismatch in Actions
- **The Issue**: Passing raw IDs (Strings) to click handlers that expect a full Data Model.
  ```kotlin
  // ❌ WRONG: UI needs to know how to toggle, but only has an ID
  onToggle = { id -> viewModel.toggle(id) } 
  ```
- **The Fix**: Pass the whole object to ensure the ViewModel has all necessary context (e.g., current state) to perform business logic.
  ```kotlin
  // ✅ CORRECT
  onToggle = { item -> viewModel.toggle(item) } 
  ```

### 4. Color Token Overuse
- **The Issue**: Using `Color.Blue` or `Color.Red` directly.
- **The Fix**: Always use `MaterialTheme.colorScheme` tokens (e.g., `primary`, `secondary`, `error`) to ensure Dark Mode compatibility.

---
[[android_build_regression_guard|🛡️ Build Guard]] | [[build_stabilization_lessons|🎓 Stabilization Lessons]]
