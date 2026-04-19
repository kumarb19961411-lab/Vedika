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
This document provides a quick reference for deterministic routing logic based on the user's role and intent. It defines how navigation branchings occur.

## Current implementation

### 🧭 The Routing Matrix

| Account Mode | Auth Flow | Scenario Check | Destination | Backstack Behavior |
| :--- | :--- | :--- | :--- | :--- |
| **USER** | **SIGN_UP** | Brand new account | **User Setup Node** | Clears OTP screen |
| **USER** | **SIGN_IN** | Existing `users/{uid}` | **Dashboard (Consumer)** | `popUpTo(0)` |
| **PARTNER** | **SIGN_UP** | Brand new account | **Vendor Registration Form** | Clears OTP screen, routes to specific categories |
| **PARTNER** | **SIGN_IN** | Existing `vendors/{uid}` & `onboardingComplete=true` | **Dashboard (Partner Home)** | `popUpTo(0)` |
| **SESSION_RESTORE** | N/A | `Splash` detects valid JWT | **Appropriate Dashboard based on cached role** | Closes Splash directly, skips Auth |

### 🔑 State Definitions

- **AccountMode**:
    - `USER`: Browsing for services. Initial setup is minimal (name only).
    - `PARTNER`: Offering services. Requires verified identities, address mapping, and storefront bootstrap.
- **AuthFlow**:
    - `SIGN_IN`: Login branch. Triggers a database hit immediately after OTP. See [[user_signin_implementation_guide]] and [[vendor_signin_implementation_guide]].
    - `SIGN_UP`: Signup branch. Defers database hit until profile form is completed.

### 🛡️ Navigation Architecture Rules

1. **Strict Backstack Clearance**: Successful registration or login MUST clear the `auth_graph` completely via `popUpTo(0)`. Partners must never be able to press "Back" from their Dashboard and end up in the OTP entry screen.
2. **Category Selection Branching (Partner Sign Up)**:
    - User selects "Venues" ➔ Sub-routes to `registration/venue`
    - User selects "Decorators" ➔ Sub-routes to `registration/decorator`
    - "Others" ➔ Routed to generic intake (Future Phase 4).
3. **Change Number Navigation**: Clicking "Change Number" on the OTP screen pops the current context and returns the user to the starting Sign In or Sign Up screen based on their original intent.

*Implementation Note: All core routing logic is strictly housed in `feature:auth:AuthViewModel.handleOtpSuccess`.*

## Future work
- Add generic intake sub-routes for "Others" category.
- Refine navigation for users holding dual roles.
