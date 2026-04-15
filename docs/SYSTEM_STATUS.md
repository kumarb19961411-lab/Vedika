# System Status — Vedika Vendor App

**Last Updated**: 2026-04-15
**Phase**: 3 (Backend Implementation & Hardening) — **COMPLETE**

## 🚀 Application Status
The Vedika vendor application has successfully transitioned from a mock-only prototype to a **Firebase-integrated** platform with a stabilized development ecosystem.

| Component | Status | Note |
| :--- | :--- | :--- |
| **Build Stability** | ✅ STABLE | `:app:assembleDevDebug` passes with Hilt/Kapt parity. |
| **Registration** | ✅ HARDENED | Explicit flow; Profile created only on successful submission. |
| **Auth** | ✅ FUNCTIONAL | Custom OTP flow with "Dev Bypass" support (1234). |
| **Calendar** | ✅ SYNCED | Strict parity between `Fake` and `Firebase` repositories. |
| **Booking Logic** | ✅ ENFORCED | Transaction-backed conflict detection for all roles. |

---

## 🔐 Firebase Backend Details

### 1. Conflict Enforcement (Authoritative)
Conflict detection is authoritative and handled via **Firestore Transactions**.
- **Rules**:
    - `FULL_DAY` conflicts with `MORNING`, `EVENING`, and `FULL_DAY`.
    - `MORNING` conflicts with `MORNING` and `FULL_DAY`.
    - `EVENING` conflicts with `EVENING` and `FULL_DAY`.
- **Precedence**: Manual date blocks take absolute precedence. Existing confirmed bookings cannot be overridden by new manual blocks without cancellation.

### 2. State Mapping
- **Pending Bookings**: Treated as **active holds** in both transactions and UI display.
- **Vendor Types**: Logic is role-aware (Venue check vs. Decorator capacity).

### 3. Security & Integrity
- **Security Rules**: Deployed to handle vendor-only writes to `bookings` and `vendors` collections.
- **Indexes**: Composite indexes configured for `vendorId + eventDate + status`.

---

## 🛠 Emulator & Testing Setup
The backend is prepared for local development via **Firebase Emulator Suite**.

- **Config**: Defined in `firebase.json`.
- **Services**: Auth, Firestore, and Rules validation.
- **Beta Readiness**: The `devDebug` variant is points to the emulator (if active) or the `dev` Firebase project, ensuring a safe sandbox for vendor beta testing.

---

## 📜 Active Documentation Map
- [**Regression Guard**](guides/android_build_regression_guard.md) — Build rules.
- [**Navigation Shell**](guides/vendor_shell_navigation.md) — Route logic.
- [**Architecture Overview**](architecture/project_structure.md) — Module map.
