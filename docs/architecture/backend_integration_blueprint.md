---
title: Backend Integration Blueprint
type: architecture
status: active
owner: Firebase Backend & Data Model Engineer
phase: Phase 3
last_updated: 2026-04-20
tags: [architecture, backend, firebase, integration]
---

# Backend Integration Blueprint

## Source of truth
The Vedika client architecture rigidly leverages a "local-first UI, remote-second data state" operational philosophy. This execution blueprint strictly maps out precisely how the established UI Jetpack Compose presentation layer structurally and methodically hooks into the external backend orchestration infrastructure, notably focusing primarily on Firebase pipelines and local emulator bindings.

## Current implementation

### 1. Hardened Core Services Instantiation
#### Firebase Authentication Pipeline
- System pipelines configure rigorously around `PhoneAuthProvider` mechanics explicitly optimized for handling Indian standard mobile numbering formatting conventions and OTP delays safely.
- Custom Backend Claim configurations (architected using Firebase Cloud Functions deployment hooks within subsequent pipeline phases) are designed to irrefutably solidify exact and un-tamperable role demarcation logic separating standard `User` objects from administrative `Vendor` structures. Currently, implicit trust spans solely boundary constraints derived structurally via explicit Document Node targeting.

#### Cloud Firestore Sub-Systems
- Operates definitively under restrictive isolation data planes designed strictly for cost reductions and targeted reads.
- **Data Plane Alpha (Administrative Vendors):** Operating context via `vendors/{uid}` target structures tracking deeply nested collections (Media node paths, structured review nodes, inventory mapping arrays). Features massive read-loads specifically accommodating consumer discovery indices.
- **Data Plane Beta (Structural Consumers):** Operates specifically under `users/{uid}` scopes. Maps tightly protected, explicitly restricted arrays containing deeply private personally identifiable characteristics. Reads and writes remain globally restricted enforcing an exact matched authentication UID condition directly inside `.rules`.

#### Isolated Emulation Test Suite
- Explicit compilation target profiles (specifically configured via module standard variant variables internally matching `devDebug`) bind local data intercept streams resolving specifically to the dedicated workstation `localhost` runtime Firebase Emulation configurations.
- All internal network references point resolutely toward configured local node loopback parameters via standard Android `10.0.2.2` hosts, completely quarantining automated GitHub Actions CI/CD functional testing actions from accidentally mutating live production index collections.

### 2. Comprehensive Synchronization Architectures
- Exclusively formulated client layer write operations execute unconditionally across specifically designated `Dispatchers.IO` isolated Coroutine scopes using opportunistic patterns. Consequently, UI component states inherently modify optimistic caching registries without explicitly awaiting network round-trips ensuring highly snappy UI operations followed implicitly by explicit Firebase `document.set()` asynchronous tracking tasks.
- Read interactions uniformly utilize Firebase Snapshot Listener integrations explicitly tied closely toward observing highly refined Repository patterns. This automatically yields lifecycle-bounded dynamic states; hence if external remote business conditions mutate successfully inside admin interfaces, connected frontend mobile layers receive differential updates precisely and uniformly without compelling arbitrary or unnecessary forced UI-reload actions.

### 3. Production Deployment Security Measures
- Deployment protocol permanently dictates strict `firestore.rules` compiler verification execution via explicit integration assertions located in standardized CI pipelines preceding any production rollout.
- Environment global variables apply absolute `allow read, write: if false;` defaults preventing catastrophic wide-open database misconfigurations and inherently rejecting stray queries unless strictly governed under carefully formulated explicitly verified structural paths.

## Future work
- Comprehensive custom domain logic Cloud Functions deployment replacing client-hosted verification checks.
