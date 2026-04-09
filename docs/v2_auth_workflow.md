# Vedika V2 Auth Workflow

This document defines the authentication and registration lifecycle for the Vedika V2 application.

## 1. Screens & Routes

| Screen | Route | Description |
| :--- | :--- | :--- |
| **Login Gateway** | `login` | Entry point for existing users/partners. |
| **Signup Gateway** | `signup` | Entry point for new users/partners. |
| **OTP Verification** | `otp_verification` | Shared verification screen for both flows. |
| **Category Selection**| `category_selection` | Step 1 of registration (Sign Up flow only).|
| **Dashboard** | `dashboard` | Primary landing after authentication. |

## 2. Flow Behavior

### A. Sign In Flow
1. **Entry**: User starts at the Login screen.
2. **State**: `AuthFlow.SIGN_IN` is initialized.
3. **Action**: User enters phone number and proceeds to OTP.
4. **Verification**: After entering correct OTP, the app navigates to the **Dashboard**.

### B. Sign Up Flow
1. **Entry**: User navigates to the Signup screen (from Login footer).
2. **State**: `AuthFlow.SIGN_UP` is initialized.
3. **Action**: User enters phone number and proceeds to OTP.
4. **Verification**: After entering correct OTP, the app navigates to **Category Selection**.

## 3. Post-OTP Routing Rules

Navigation decision is driven by the `AuthFlow` state in `AuthViewModel`:

```kotlin
val route = if (authState.authFlow == AuthFlow.SIGN_UP) {
    VedikaDestination.CategorySelection.route
} else {
    VedikaDestination.Dashboard.route
}
```

## 4. Interaction Guardrails
- **Back Navigation**: "Change Number" on the OTP screen pops the backstack, returning the user to their originating screen (Login or Signup) while preserving their input state.
- **Stable Initialization**: `AuthFlow` is set via `LaunchedEffect(Unit)` on screen entry to ensure it doesn't change unexpectedly during transient UI updates.
- **Account Mode**: The `LoginMode` (User vs Partner) is preserved across screens to ensure the correct context is maintained during registration.

## 5. Mock / Dev Behavior
- **OTP**: Use `1234` for successful verification in dev mode.
- **Shared State**: The `AuthViewModel` is shared across the login, signup, and OTP screens. This ensures:
    - The correct phone number is displayed on the OTP screen.
    - The explicit routing intent (`AuthFlow`) is preserved.
- **Timer**: 30-second countdown for resend.
- **Bypass**: "Developer Route (Bypass)" on the Login screen skips the entire auth flow and goes directly to the Dashboard.

## 6. Future Real Auth Extensions
- When moving to Firebase Auth, the `AuthFlow` state will remain valid as a UI-layer coordinator.
- The `isNewPartner` logic from the backend can serve as a fallback, but explicit flow intent (Sign In vs Sign Up) should remain the primary router.
