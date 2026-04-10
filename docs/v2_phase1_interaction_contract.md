# Interaction Contract — Vedika V2 Phase 1

This contract defines the exact expected behavior for every interactive element in the Phase 1 registration flow.

## 1. Login & Signup Screens
- **Role Toggles**: Switching between User/Partner must instantly update the `AccountMode` in the ViewModel.
- **Phone Input**: Must only accept numeric characters. Formatting `+91` should be handled automatically by the UI decorator.
- **CTA (Send OTP)**: Disabled until a 10-digit number is entered. Navigates to `auth/otp` on success.
- **Footer Links**:
- **Copy Specifications**:
    - **Login**: Title: `Sign In` | Subtitle: `Access your account to book or manage event services` | Toggles: `User Login`, `Partner Login` | Footer: `New to KalyanaVedika? Register your account`.
    - **Signup**: Title: `Sign Up` | Subtitle: `Create your account to book or provide event services` | Toggles: `User Sign Up`, `Partner Sign Up` | Footer: `Already have an account? Sign In`.
    - **Role Helper (Both)**: `Partner` -> `Provide and manage your services` | `User` -> `Book services for your event`.

- **Post-Verification Destination Matrix**:
    - **USER** + (SignIn OR SignUp) ➔ **Dashboard**
    - **PARTNER** + SignIn ➔ **Dashboard**
    - **PARTNER** + SignUp ➔ **Category Selection**

### 1.1 OTP Verification Screen
- **Input Behavior**:
    - Uses 4 individual slots for numeric entry.
    - Focus must automatically land on the first slot if empty.
    - **Verify** button is disabled until exactly 4 digits are entered.
- **Back / Change Number**:
    - Triggers a return to the **Origin Screen** (Login if `SIGN_IN`, Signup if `SIGN_UP`).
- **Resend OTP**:
    - Triggerable only after a 30s countdown.
    - Results in a success message and timer reset in Mock/Dev mode.

## 2. Category Selection Screen
- **Bento Cards**:
    - Single-selection only.
    - Selecting a card highlights it with a colored border and check icon.
    - Selecting "Venues" vs "Decorators" must update the `selectedCategory` in state.
- **Proceed Button**: Enabled only when a category is selected. Navigates to the specific registration form based on selection.

## 3. Registration Forms (Venue & Decorator)
- **Validation**:
    - **Venue**: "Continue" is enabled only if `Venue Name` AND `Location` are not blank.
    - **Decorator**: "Complete Registration" is enabled only if `Business Name` is not blank.
- **Pricing Fields**: Must trigger the `Number` keyboard layout.
- **Feedback (Premium)**:
    - **Save Draft**: Must trigger a Snackbar with "Draft saved successfully".
    - **Complete Registration**: Must trigger a Snackbar with "Registration Complete! Welcome to Vedika" before delayed navigation to Dashboard.
- **Skip**: Clicking "Skip" bypasses the form and navigates directly to Dashboard (Mock behavior for Phase 1).

---

## 🛑 Mock Safety Rules
1. **No Backend Persistence**: In Phase 1, data is only saved in the `UiState` and `AuthViewModel`. Clicking "Save Draft" does not write to a database yet.
2. **Deterministic OTP**: Verification only succeeds if `1234` is entered.
3. **Dashboard Access**: The "Dashboard" is currently a terminal destination (Placeholder) and does not require session verification in Phase 1 dev mode.
