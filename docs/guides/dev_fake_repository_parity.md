---
title: Fake Repository Parity Guide
type: guide
status: active
owner: All
phase: Phase 3
last_updated: 2026-04-21
tags: [dev, data, repository, fake, testing]
---

# 💾 Fake Repository Parity Guide

In Vedika, we use **Fake Repositories** to enable fast feature previews and reliable unit testing without needing a live network or Firebase connection. Maintaining parity between the Production Interface and these Fakes is mandatory.

## 🏗️ Structure
Repository contracts are defined in `:core:data`.
- **Interface**: `VendorRepository.kt`
- **Prod Implementation**: `FirestoreVendorRepository.kt`
- **Fake Implementation**: `FakeVendorRepository.kt`

## ⚖️ The Parity Rule
**"Any change to the Interface MUST be matched in the Fake implementation in the same PR."**

### Common Failure Patterns
1. **Method Signature Change**: Adding a parameter to `toggleAvailability(id)` but leaving `FakeVendorRepository` with the old signature.
2. **Return Type Change**: Changing `Result<T>` to `Flow<T>` in the interface but failing to update the Fake's return type.
3. **New API Methods**: Adding `getVendorProfileStream()` to the interface but not providing a stub in the Fake.

## 🛠️ How to Sync
1. Open the Repository Interface in `:core:data`.
2. Apply your changes.
3. Use the IDE's "Implement Members" feature on `FakeVendorRepository` to quickly identify missing or mismatched overrides.
4. Ensure the Fake returns a **predictable, stable default value** (e.g., `Result.success` or `flowOf(emptyList())`) to avoid breaking feature previews.

## 🧪 Verification
- Run a targeted compile on the module containing the fake:
  ```powershell
  ./gradlew :core:data:compileDebugKotlin
  ```
- If this fails, typically the Fake repository is the culprit.

---
[[android_build_regression_guard|🛡️ Build Guard]] | [[build_stabilization_lessons|🎓 Stabilization Lessons]]
