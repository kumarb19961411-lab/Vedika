---
title: Canonical Auth Master Workflow
type: architecture
status: active
owner: Security Access Guard
phase: Phase 3
last_updated: 2026-04-20
tags: [architecture, auth, workflow, security]
---

# Canonical Auth Master Workflow

## Source of truth
This document acts as the absolute architectural source of truth defining deterministic app routing, data integration boundaries, and critical state transitions for all User and Vendor authentication flows across the Vedika platform. All internal logic built within the `:feature:auth` module, and any module interacting with it, must strictly abide by these documented patterns to prevent fragmented backstack routing and session capability leaks.

## Current implementation

### 1. Vendor Flow (Partner Side)
Vendor accounts operate under a strict, multi-step profile construction layer. Partner accounts must achieve full profile completion before crossing the application boundary into terminal dashboard states.
- **Sign Up Mechanics:** Vendor intent initiates by supplying a valid phone number. The gateway fires a Firebase SMS OTP mechanism. Upon matching verification, the system securely caches the immediate `uid` locally and routes the navigation graph explicitly to the specialized Onboarding Flow (Vendor Profile Bootstrap) based on their targeted category (Venue, Decorator, etc). Only upon validated form submission does a true Document representation get instantiated into `vendors/{uid}`. Finally, `popUpTo(0)` clears the graph and exposes the Dashboard.
- **Sign In Mechanics:** Existing vendor enters their phone number and verifies via OTP. The core `AuthViewModel` seamlessly executes an asynchronous query against the master `vendors/{uid}` target. It asserts the presence of the document and an explicit `onboardingComplete=true` flag. If authenticated successfully through these bounds, `popUpTo(0)` routes directly to the operational Partner Dashboard. If the document states incomplete, logic forces the vendor back into their specific setup sequence intercept point.

### 2. User Flow (Consumer Side)
User (Consumer) entry flows fundamentally prioritize absolute operational velocity and low-friction engagement over profound profile depth. 
- **Sign Up Mechanics:** Given a new consumer phone number targeting signup, OTP verification instantly reroutes to a frictionless User Setup node (requesting merely standard display name elements). Submitting the payload creates the barebones `users/{uid}` index, and routes immediately to the broader application layout via `popUpTo(0)`.
- **Sign In Mechanics:** User submits previously saved phone number; OTP verification success executes an immediate backend query to validate `users/{uid}` existence. Affirmative validation routes immediately to the primary Dashboard.

### 3. Session Restoration & Interception Handshake
Session restoration optimizes return velocity to a near-zero latency constraint.
- **Cold App Launch:** Boot sequence utilizes `SplashViewModel` instantly intercepting primary navigation mapping to parse local `EncryptedSharedPreferences` for unexpired JWT structures representing a valid, live session token. 
- **Validation Hit:** Provided the token evaluates successfully and is firmly bound to a resolvable `cachedRole`, standard application splashing is completely bypassed. Backend observation streams lock onto target indices immediately, and app routes cleanly to specific UX variants without drawing any un-authenticated fragment layouts.
- **Validation Miss:** Token decay, structural corruption, or null representations safely obliterate existing validation blocks and instantly yield to initial global Login gating screens.

### 4. Database Role Constraints
- Application roles (Partner vs User) operate conceptually as physical planes governed firmly beneath structural `firestore.rules` rule-bindings. For instance, any internal UI layer operating with `AccountMode == PARTNER` will face severe access denied execution errors if it attempts unauthorized mutations targeting a foreign `users/{uid}` sub-tree.

## Future work
- Introduce structured multi-factor authentication scaling endpoints specifically targeting high-volume financial Vendor instances.
- Architect dynamic, scope-toggling primitives allowing seamless session conversion for identities performing dual operations as both standard system consumers and structural partners without enduring explicit terminal log-outs.
