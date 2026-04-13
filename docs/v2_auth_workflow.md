# Auth Workflow Logic Tree — Vedika V2

This document defines the deterministic routing and state transitions for the Vedika V2 authentication and onboarding flow.

## 📈 The Journey (Golden Path)

### 1. The Entry Branch
There are two distinct entry points on the **Login** screen:
- **Case A: Sign In** (Return User/Partner) ➔ `auth/login`
- **Case B: Sign Up** (New User/Partner) ➔ `auth/signup`

### 2. OTP Verification (Unified Link)
Both Case A and B consolidate at the **OTP Verification** screen (`auth/otp`).
- **Dev Mode**: Use `1234` for instant verification.
- [**RULE**]: The `AuthViewModel` must preserve the `authFlow` and `accountMode` across this transition.

### 2.1 Back Navigation (Change Number)
- **Flow: SIGN_IN** ➔ Returns to **Login Screen** (`auth/login`).
- **Flow: SIGN_UP** ➔ Returns to **Signup Screen** (`auth/signup`).

### 3. Post-Verification Routing (Deterministic)

| Entry Flow | Account Mode | Next Destination | Note |
| :--- | :--- | :--- | :--- |
| **SIGN_IN** | **USER** | **Dashboard** | |
| **SIGN_UP** | **USER** | **Dashboard** | Self-service Setup |
| **SIGN_IN** | **PARTNER** | **Dashboard** | Partner Home |
| **SIGN_UP** | **PARTNER** | **Category Selection** | Start Onboarding |

### 4. Registration Sub-graph (Partner Signup Only)
1. **Category Selection**: Partner chooses their vendor profile (Venues, Decorators, etc.).
2. **Category Branching**:
    - **Selection: Venues** ➔ `registration/venue`
    - **Selection: Decorators** ➔ `registration/decorator`
3. **Completion**: Navigates to `dashboard` via `popUpTo(AuthGraph)`.

---

## 💾 State Preservation (Shared ViewModel)
To ensure zero data loss during onboarding, the following state is tracked in the `AuthViewModel` scoped to the `auth_graph`:
- `ownerName`: Captured during Signup.
- `phoneNumber`: Prefilled for correction.
- `authFlow` / `accountMode`: Branch controls.
- `selectedCategory`: Used for branching.

---

## 🏗 Implementation Pattern
Auth logic relies on a single source of truth in `AuthViewModel.handleOtpSuccess()`. This method calculates the next destination based on the cached state variables, ensuring that role-aware routing is centralized and testable.

*Reference: [Role Behavior Matrix](role_behavior_matrix.md) for a condensed view.*
