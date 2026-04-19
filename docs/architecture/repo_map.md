---
title: Repository Map & Topology
type: architecture
status: active
owner: Product Architect
phase: Phase 3
last_updated: 2026-04-20
tags: [architecture, repo-map, modules, structure]
---

# Repository Map & Topology

## Source of truth
The Vedika project enforces a strict, compilation-enforced multi-module structure. This prevents un-intended feature crossover and keeps our gradle build times low. This document is the canonical map of these boundaries.

## Current implementation

### The App Boundary (`:app`) 
- **Responsibility:** Application shell, dependency injection wiring (via Hilt), environment constants, and the `MainActivity`.
- **Constraint:** Should have minimal pure logic. Only houses UI Navigation scaffolding if cross-feature.

### Feature Modules (`:feature:*`)
- **`:feature:auth`**
  - **Responsibility:** Handles Introductory branding, Sign In, Sign Up, Verification, and initial Role Bootstrapping. Sub-graphs into OTP paths. See [[auth_master_workflow]].
  - **Outputs:** Emits fully authenticated states which the `:app` shell listens to.
- **`:feature:dashboard`**
  - **Responsibility:** The centralized hub post-authentication. Contains separate but structurally parallel shells for Users and Vendors.

### Core Modules (`:core:*`)
- **`:core:ui`**
  - **Responsibility:** Design system components, Material 3 wrappers, standard typography, color tokens, and canonical spacings.
- **`:core:data`**
  - **Responsibility:** Repository abstraction layer. Network clients, Firebase DB injection wrappers, local `EncryptedSharedPreferences` caches.
  - **Constraint:** Features never query Firebase directly. They query `core:data` repositories.
- **`:core:domain`**
  - **Responsibility:** Contains Pure Kotlin UseCases mapping cross-module interactions (e.g. `ValidateVendorRegistrationUseCase`).

## Future work
- Introduce stricter separation of `core:data` into distinct domain-level repositories.
- Extract common navigation logic into `:core:navigation`.
