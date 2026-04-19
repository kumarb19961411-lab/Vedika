# Repository Map & Topology

The Vedika project enforces a strict, compilation-enforced multi-module structure. This prevents un-intended feature crossover and keeps our gradle build times low.

## The App Boundary (`:app`) 
- **Responsibility:** Application shell, dependency injection wiring (via Hilt), environment constants, and the `MainActivity`.
- **Constraint:** Should have minimal pure logic. Only houses UI Navigation scaffolding if cross-feature.

## Feature Modules (`:feature:*`)
- **`:feature:auth`**
  - **Responsibility:** Handles Introductory branding, Sign In, Sign Up, Verification, and initial Role Bootstrapping. Sub-graphs into OTP paths.
  - **Outputs:** Emits fully authenticated states which the `:app` shell listens to.
- **`:feature:dashboard`**
  - **Responsibility:** The centralized hub post-authentication. Contains separate but structurally parallel shells for Users and Vendors.

## Core Modules (`:core:*`)
- **`:core:ui`**
  - **Responsibility:** Design system components, Material 3 wrappers, standard typography, color tokens, and canonical spacings.
- **`:core:data`**
  - **Responsibility:** Repository abstraction layer. Network clients, Firebase DB injection wrappers, local `EncryptedSharedPreferences` caches.
  - **Constraint:** Features never query Firebase directly. They query `core:data` repositories.
- **`:core:domain`**
  - **Responsibility:** Contains Pure Kotlin UseCases mapping cross-module interactions (e.g. `ValidateVendorRegistrationUseCase`).
