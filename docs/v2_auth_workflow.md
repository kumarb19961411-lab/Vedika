# Auth Workflow Logic Tree — Vedika V2

This document defines the deterministic routing and state transitions for the Vedika V2 authentication and onboarding flow.

## 📈 The Journey (Golden Path)

### 1. The Entry Branch
There are two distinct entry points on the **Login** screen:
- **Case A: Sign In** (Return User/Partner)
    - **Intent**: Access existing dashboard.
    - **UI Route**: `auth/login`
- **Case B: Sign Up** (New User/Partner)
    - **Intent**: Create a new account and register services.
    - **UI Route**: `auth/signup` (Accessed via "Register your account" footer).

### 2. OTP Verification (Unified Link)
Both Case A and B consolidate at the **OTP Verification** screen (`auth/otp`).
- **Dev Mode**: Use `1234` for instant verification.
- [**RULE**]: The `AuthViewModel` must preserve the `authFlow` (`SIGN_IN` vs `SIGN_UP`) and `loginMode` (`USER` vs `PARTNER`) across this transition.

### 3. Post-Verification Routing (Deterministic)
The destination after a successful OTP verification depends entirely on the entry branch:

| Entry Mode | Verification Status | Next Destination |
| :--- | :--- | :--- |
| **SIGN_IN** | SUCCESS | **Dashboard** |
| **SIGN_UP** | SUCCESS | **Category Selection** |

### 4. Registration Sub-graph (Signup Only)
If the user is in `SIGN_UP` mode, they follow this branch:
1. **Category Selection**: User chooses their vendor category (Venues, Decorators, etc.).
2. **Category Branching**:
    - **Selection: Venues** -> Navigates to `registration/venue`
    - **Selection: Decorators** -> Navigates to `registration/decorator`
    - **Other Categories** -> (Future Work) Maps to generic registration.

---

## 💾 State Preservation Guide
To ensure a "Premium" feel, the following state must be tracked in the **Shared `AuthViewModel`**:
- `phoneNumber`: String (Prefilled if returning from Back stack).
- `authFlow`: `AuthFlow` Enum (`SIGN_IN`, `SIGN_UP`).
- `accountMode`: `AccountMode` Enum (`USER`, `PARTNER`).
- `selectedCategory`: `ServiceCategory?` (Preserved if navigating back from a registration form).

---
*Note: Any modification to the `MainActivity` NavHost logic must be audited against this workflow.*
