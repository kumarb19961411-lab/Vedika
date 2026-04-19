---
title: Role Behavior & Routing Matrix
type: architecture
status: active
owner: Product Architect
phase: Phase 3
last_updated: 2026-04-20
tags: [architecture, routing, core, roles]
---

# Role Behavior & Routing Matrix

## Source of truth
This matrix operates as the central execution reference for modeling correct navigation transitions driven dynamically by the specific authenticated user role and explicit interaction intent. It guarantees determinism resolving Jetpack Compose navigation branches effectively and safely.

## Current implementation

### 🧭 The Execution Routing Framework

| Active Client Account Mode | Intentional Auth Flow | Operational Scenario Result | Navigational Destination Resolution | Active Backstack Constraints |
| :--- | :--- | :--- | :--- | :--- |
| **USER_TIER** | **SIGN_UP_REQUEST** | System validates a genuinely nonexistent account request | **Immediate User Context Node** | System purges OTP sequence nodes entirely |
| **USER_TIER** | **SIGN_IN_REQUEST** | Validation captures existing explicit `users/{uid}` layer | **Consumer Dashboard Shell** | Executes complete global `popUpTo(0)` |
| **PARTNER_TIER** | **SIGN_UP_REQUEST** | Operation confirms an unfamiliar root identity parameter | **Dynamic Vendor Registry Hub** | Terminates base OTP trace, targets category spec bounds |
| **PARTNER_TIER** | **SIGN_IN_REQUEST** | Discovery locates exact `vendors/{uid}` & `onboarding=true` params | **Vendor Control Dashboard** | Executes complete global `popUpTo(0)` |
| **SESSION_RESTORE** | N/A (Bypassed) | Initial Boot `Splash` detects perfectly sound JWT states | **Dependent Dashboard (Mapped via Enum)** | Force-skips UI introduction entirely |

### 🔑 Critical State Definitions

- **Core Application Account Modes**:
    - `USER`: Identities utilizing tools and browsing vendor services. Initial application setup thresholds remain minimal intentionally (Name captures only).
    - `PARTNER`: Vendor accounts extending primary services to system layers. Mandates heavily verified profile schemas, robust address tracking layers, and exhaustive primary storefront definitions before unlocking access.
- **Transitional Authentication Flows**:
    - `SIGN_IN_INTENT`: Direct login sequences. By definition, heavily prioritizes triggering exhaustive network database validation pings directly following OTP conclusion.
    - `SIGN_UP_INTENT`: Constructive branch mechanics. Deliberately delays aggressive remote validation sequences pushing instead into localized setup pipelines until complete schemas are constructed representing new entities.

### 🛡️ Rigid Navigational Architecture Governance

1. **Backstack Total Annihilation Policy**: An explicitly finalized vendor registration structure, or simply a correct authorization match MUST purge and clear off the totality of the `auth_graph` through immediate global navigation commands. Vendor partners must never retain the physical back-button ability to reverse out of secured control screens directly into loose OTP form contexts.
2. **Category Branch Escalation (Partner Origin Forms)**:
    - User asserts category "Venues" intent ➔ Local graph dynamically swaps into intensive `registration/venue_module` layers.
    - User asserts category "Decorators" intent ➔ Application sub-routes accurately to tailored `registration/decorator_module` paths.
3. **Change Operational Context Escaping**: Selecting any "Change Origin Context" interaction within deep OTP frames forces an explicit back-stack termination, yielding immediately to previously instantiated logical start zones (either primary Registration or Login anchors).

## Future work
- Integrate advanced intake form components effectively spanning comprehensive generic intake systems mapped dynamically using configurable UI primitives (e.g. backend-driven UI schema intakes for loosely defined roles).
