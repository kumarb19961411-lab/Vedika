# Phase 2 Backlog & Deferred Items

### Incomplete Phase 1 Carryovers
- None directly blocking. All views and flows are mapped properly to Compose.

### Deferred Firebase/Backend Features
- **Trusted Enforcement Work**: Booking status transitions and inventory availability locking must eventually shift to Firebase Cloud Functions for trusted integrity. They are currently implemented via client-side SDK calls in Phase 2 logic (marked explicitly lacking backend enforcement).
- **Google Sign-In**: Deferred. Authentication supports mock Bypass + scaffolded OTP (to be hooked up later).
- **Finance Ledger & Payouts**: Display only. Client-authoritative mutations are strictly prohibited.
- **Gallery Uploads (Storage)**: Unfinished. Requires future Firebase Storage integration.

### QA Gaps
- End-to-end integration tests using Local Firebase Emulators.
