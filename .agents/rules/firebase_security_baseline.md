# Firebase & Android Security Baseline

## Purpose
This document establishes the workspace-wide Firebase and Android security baseline. It dictates the minimum acceptable security posture, access models, and data protection requirements for the Vedika app.

## Scope
These rules apply to all Firebase configurations, Backend rules, Cloud Functions, data persistence mechanisms, Android local storage capabilities, and client-side access constraints.

## Identity Baseline
- **Firebase Auth Required:** Firebase Authentication forms the absolute, uncompromising basis for all protected identities (Users, Vendors, Admins). Custom identity paradigms bypassing Firebase Auth are rejected.

## Authorization Baseline
- **Deny-by-Default:** Systemic access posture is perpetually closed unless specifically pierced via verified, explicit rule logic.
- **Role-Aware Authorization:** Authorization mechanisms must inherently understand context (e.g., vendor, user, admin).
- **No UI-Only Authorization:** Security models relying entirely upon visual app toggles or "hidden buttons" are banned.

## Firestore/Storage Rule Baseline
- Access must be protected fundamentally via explicitly drafted Firebase Security Rules.
- Rules must restrict data read/write capabilities strictly relative to the authenticated payload ID and role Custom Claims.
- Open, broad wildcard access arrays are banned in production.
- Security rule design must remain deliberate, intentional, strictly scoped, and externally reviewable.

## App Check Baseline
- Firebase App Check must be actively configured and enforced as part of the primary production backend protection array, ensuring responses are dispatched only to valid client binaries.

## Trusted Backend Operation Baseline
- Sensitive, volatile, or highly privileged operations must funnel strictly through trusted backend logic (Cloud Functions), not assumed client honesty.
- Payout dispatches, ledger settlements, role mutations, privileged overrides, and cross-party state changes **cannot** rely on untrusted direct client commands.

## Sensitive Data Handling Baseline
- Vendor data must inherently remain quarantined from leaking across vendor boundaries.
- Banking logic, KYC assets, IFSC strings, UPI layouts, and similar identifiers require stringent access tracking and minimal retrieval horizons.
- Production implementations must rigorously isolate sensitive interactions from casual caching mechanisms.

## Android Local Storage Baseline
- **No Plaintext Secrets:** Sensitive cryptographic tokens, credentials, and financial/KYC data must never map to plaintext storage files on the Android device.
- Local capabilities targeting credentials must deploy natively secured vaults (Keystore/EncryptedSharedPreferences).

## Logging, Analytics & Crash Hygiene Baseline
- Sensitive values categorically must never surface via crash reports, non-encrypted telemetry, or observable logs.
- Bank records, Phone numbers, Identity identifiers, IFSC/UPI markers, and payout states must be scrubbed utilizing automated regex redactions.

## Review Expectations
- Feature branches invoking financial transit, administrative routing, Payout requests, or structural cross-user logic are universally barred without a comprehensive signoff from the Security Access Guard.

## Non-Negotiable Constraints
- **NO** authorization models governed purely by UI-state manipulation.
- **NO** unvetted admin-only actions exposed merely through UI parameters.
- **NO** Payout, Settlement, or administrative decisions deployed utilizing direct untrusted Android device origins.
- **NO** emulator, debug, or integration testing bypass configurations deploying unintentionally inside production pipelines.

## Anti-Patterns To Reject
Block and purge the following behaviors:
- Permissive wildcard Firestore/Storage Rules (`allow read, write: if request.auth != null;`).
- Broad media read/write Cloud Storage paths.
- "Hide the UI element" passed off as security protocol.
- Plaintext persistence involving active banking arrays inside core device storage.
- Bleeding core PII (KYC, phone #s, UPIs, passwords, payout numbers) across Logging lines.
- Debug security logic natively bypassing App Check within production domains.
- Unreviewed manual or "admin-only" internal structural bypasses.
- Flimsy protections surrounding trusted backend logic execution.
