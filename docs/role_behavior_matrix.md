# Role Behavior & Routing Matrix — Vedika V2

This document provides a quick reference for deterministic routing logic based on the user's role and intent (Sign In vs. Sign Up).

## 🧭 The Routing Matrix

| Account Mode | Flow | OTP Success Destination | Final Destination |
| :--- | :--- | :--- | :--- |
| **USER** | **SIGN_IN** | Post-OTP Check | **Dashboard** (Consumer) |
| **USER** | **SIGN_UP** | Post-OTP Check | **Dashboard** (Self-service Setup) |
| **PARTNER** | **SIGN_IN** | Post-OTP Check | **Dashboard** (Partner Home) |
| **PARTNER** | **SIGN_UP** | **Category Selection** | **Registration Form** ➔ **Shell**|

## 🔑 State Definitions

- **AccountMode**:
    - `USER`: Browsing for services. Bypasses registration.
    - `PARTNER`: Offering services. Requires registration if signing up.
- **AuthFlow**:
    - `SIGN_IN`: Login branch.
    - `SIGN_UP`: Signup branch.

## 🛡️ Navigation Rules

1. **Backstack Clearance**: Successful registration MUST clear the `auth_graph` to prevent partners from navigating back into onboarding forms.
2. **Category Selection Branching**:
    - "Venues" ➔ `registration/venue`
    - "Decorators" ➔ `registration/decorator`
    - "Others" ➔ Generic registration (Future).
3. **Change Number Navigation**: Clicking "Change Number" on the OTP screen returns the user to **Sign In** or **Sign Up** based on their initial entry branch.

---
*Implementation Note: Routing logic is strictly housed in `feature:auth:AuthViewModel.handleOtpSuccess`.*
