---
title: Session Restoration Strategy
type: architecture
status: active
owner: Security Access Guard
phase: Phase 3
last_updated: 2026-04-20
tags: [architecture, auth, session, security]
---

# Session Restoration Strategy

## Source of truth
This document explicitly and practically formulates the foundational methodologies to systematically drive perceived functional application load latency metrics effectively towards absolute zero constraints specifically regarding securely authenticated returning application profiles.

## Current implementation

### 1. Robust Cryptographic Persistence Cache
The foundational and exclusive driver controlling instantaneous valid session restoration logic relies uncompromisingly upon secure native `EncryptedSharedPreferences` instantiations which securely invoke specific underlying Android keystore environments directly traversing safe application bootstrap cycles.
- Persisted Execution Assets:
  1. `accessToken` (Cryptographically verified JWT schema directly formulating structural proof matching an active concurrent live session instance).
  2. `refreshToken` (Secure long-term proxy hash providing explicit capabilities authorizing entirely silent background token-renewal flows).
  3. `uid` (The absolute structural backend registry pointer strictly linking app-state parameters directly to Firestore).
  4. `cachedRole` (Enum String securely designating fundamental constraints mapping directly as `VENDOR` or `USER`).

### 2. Strict Deterministic Lifecycle Evaluations
1. **Target App Bootstrap:** Instantiation via `MainActivity` globally invokes UI mapping layers relying primarily on an immediate fast-path `SplashViewModel` hook execution.
2. **Synchronous Validation Thresholds:** Deep `SplashViewModel` actions instantly acquire protected preferences attempting explicit parsing matching system clock invariants.
3. **Primary Resolution — Hit:** Should `accessToken` variables execute favorably parsing standard active time thresholds natively, top-level `NavHostController` interactions instantly deploy global `popUpTo(0)` routines totally purging deep historical traces. App subsequently diverts completely matching mapped `cachedRole` structures bypassing explicit auth modules universally.
4. **Secondary Deflection — Renewal Mechanism:** Should local parameter validation flag potentially stale or recently degraded JWT data markers correctly, an isolated background execution instantly leverages the explicit cached `refreshToken` string seamlessly targeting dedicated Firebase Auth renewal pipelines securely. Upon flawless network renewal logic completions, all active state bindings inherently resolve gracefully launching users explicitly toward core Dashboard nodes exactly as a standard hit.
5. **Critical Resolution — Hard Miss:** Failed backend background renewals securely default alongside catastrophic invalid token findings automatically tearing down local preference blocks and explicitly terminating secure channels. Application then explicitly mounts baseline `AuthIntro` layouts demanding full authentication procedures normally.

### 3. Paramount Data Sensitivity Controls
At absolutely no phase of explicit functionality executes persistent preservation involving specific `users/{uid}` deeply nested raw payload attributes (such as PI values, identifiable addresses, or plaintext credentials) anywhere within local isolated Encrypted Preferences frameworks. State representations are singularly bounded to explicitly generic UUID routing maps and role demarcations. Executing standard operating system clear-data functionality natively annihilates preferences thereby entirely decommissioning the specific security logic implicitly and reliably.

## Future work
- Introduce fine-grained metrics telemetry targeting potential background renewal latency variations.
