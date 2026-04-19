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
The Vedika project enforces a strict, compilation-enforced multi-module structure utilizing standard Android plugin conventions. This structure exists to aggressively prevent un-intended feature crossover, strictly encapsulate domain boundaries, and keep iterative Gradle build times exceptionally low. This document is the canonical map of these boundaries and module interactions.

## Current implementation

### The App Boundary (`:app`) 
- **Responsibility:** Operates as the application shell. Manages primary dependency injection wiring using Hilt, establishes the ultimate execution environment constants, and hosts the top-level `MainActivity` scaffolding the overarching NavHost.
- **Constraint:** Must contain zero pure business logic. Only exists to marshal isolated feature boundaries together through centralized Jetpack Navigation controllers.

### Feature Subsystems (`:feature:*`)
- **`:feature:auth`**
  - **Responsibility:** Manages all entry flow transitions, introductory app branding, Sign In mechanisms, comprehensive User and Vendor Sign Up protocols, OTP Verification logic, and initial Role Bootstrapping. It dictates initial sub-graph logic.
  - **Outputs:** Exclusively emits validated, fully authenticated JWT state representations which the root `:app` layer subsequently listens to for top-level graph swapping.
- **`:feature:dashboard`**
  - **Responsibility:** Acts as the centralized, authenticated hub. Contains isolated, structurally specialized shells for `User` consumers versus `Partner` vendors, displaying targeted feature widgets.

### Core Libraries (`:core:*`)
- **`:core:ui`**
  - **Responsibility:** Home to our canonical design system components, comprehensive Material 3 wrappers, rigid typography definitions, curated color tokens, and universally applied spacing modifiers. It holds no application state.
- **`:core:data`**
  - **Responsibility:** Houses all repository abstractions, mapping interfaces to functional API or DB layers. Contains Retrofit network clients, generalized Firebase Database injection wrappers, and highly-secure local persistence through `EncryptedSharedPreferences`. 
  - **Constraint:** Features **never** directly invoke external APIs or Firebase singletons. They communicate strictly through decoupled interfaces provided by `:core:data`.
- **`:core:domain`**
  - **Responsibility:** Contains Pure Kotlin UseCases specifically mapping robust cross-module constraints and heavy data translations that span beyond simple CRUD actions (e.g., `ValidateVendorRegistrationUseCase`).

## Future work
- Introduce stricter separation inside `core:data`, subdividing it logically by feature entity (e.g., `:core:data:bookings`, `:core:data:catalogs`).
- Aggressively extract redundant Jetpack Navigation boilerplate into a dedicated `:core:navigation` module to simplify `:app` level implementations mapping.
