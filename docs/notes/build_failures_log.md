---
title: Build Failures Log
type: notes
status: active
owner: All
phase: Phase 3
last_updated: 2026-04-21
tags: [notes, build, debug, log]
---

# 📝 Build Failures Log

Historical tracking of build symptoms and their definitive resolutions.

| Date | Symptom | Component | Root Cause | Resolution |
| :--- | :--- | :--- | :--- | :--- |
| 2026-04-21 | `jlink` executable not found | Gradle Transforms | JDK mismatch (`JdkImageInput`) | Pinned `org.gradle.java.home` to Android Studio JBR. |
| 2026-04-21 | Unresolved reference: `AuthRepository` | ViewModels | Missing imports post-refactor | Manually added imports to `ProfileViewModel.kt`. |
| 2026-04-21 | Smart cast is impossible on `user` | `MainActivity.kt` | Mutable state with `if (user is ...)` | Added explicit `user as Authenticated` cast in branch. |
| 2026-04-21 | No value passed for parameter `item` | `InventoryScreen.kt` | Repo/UI signature mismatch | Updated `toggleAvailability` to pass `InventoryItem`. |
| 2026-04-21 | `Icon` parameter `size` issues | `InventoryHubScreen` | Material 3 Icon constraint | Moved size to `Modifier.size()`. |
| 2026-04-21 | Build successful but JVM changed | Gradle Cache | Stale cache after JDK swap | Cleared `.gradle` metadata. |

---
[[android_build_regression_guard|🛡️ Build Guard]] | [[build_stabilization_lessons|🎓 Stabilization Lessons]]
