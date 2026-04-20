---
title: Build Stabilization Lessons
type: notes
status: active
owner: All
phase: Phase 3
last_updated: 2026-04-21
tags: [lessons, architecture, stabilization, review]
---

# 🎓 Build Stabilization Lessons

Key takeaways from the Milestone 1 closeout audit to prevent recurring technical debt.

## 🧠 Core Lessons

### 1. Fake Repository Parity (CRITICAL)
- **Symptom**: Features (Calendar, Booking) failing to compile despite "production" repos being clean.
- **Lesson**: Our architecture uses `Fake*Repository` implementations for feature-specific previews and local dev targets. If the production interface changes, these fakes often become silent blockers.
- **Rule**: **When changing repository interfaces, also check and update fake repos.**

### 2. Semantics of Asynchronous Streams
- **Symptom**: `GalleryViewModel` expecting `Result<T>` while the repository emitted `Flow<T>`.
- **Lesson**: Interface contracts must explicitly define if they are one-shot (`suspend fun -> Result`) or streaming (`fun -> Flow`).
- **Rule**: **Do not assume Flow vs Result semantics.** Always verify the contract in `:core:data`.

### 3. Smart-Cast Safety in Scoped States
- **Symptom**: "Smart cast is impossible" on the `user` object in `MainActivity`.
- **Lesson**: Kotlin cannot guarantee that a mutable state property hasn't changed between a check and its usage.
- **Rule**: Within specific auth-state branches (e.g., `Authenticated`), use local variables or explicit casting to satisfy the compiler and ensure type safety.

### 4. UI/Model Tight Coupling
- **Symptom**: `InventoryScreen` failing due to primitive-only argument passing instead of the expected `InventoryItem` model.
- **Lesson**: Primitive sprawl in function signatures leads to brittle refactoring.
- **Rule**: Prefer passing the Data Model itself to UI interaction handlers (e.g., `toggle(item)` instead of `toggle(id, bool)`).

## 🛡️ Preventive Action Items
- [ ] Add `check_fakes` to the PR checklist.
- [ ] Pinned JDK Toolchain in repo root to enforce JBR usage.
- [ ] Introduced `android_build_regression_guard` to the developer onboarding flow.

---
[[android_build_regression_guard|🛡️ Build Guard]] | [[build_failures_log|📝 Failures Log]] | [[dev_fake_repository_parity|💾 Fake Repo Parity]]
