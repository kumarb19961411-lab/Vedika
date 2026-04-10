# Interaction Contract — Vedika V2 Phase 1

This contract defines the exact expected behavior for every interactive element in the Phase 1 registration flow.

## 1. Login & Signup Screens
- **Role Toggles**: Switching between User/Partner must instantly update the `AccountMode` in the ViewModel.
- **Phone Input**: Must only accept numeric characters. Formatting `+91` should be handled automatically by the UI decorator.
- **CTA (Send OTP)**: Disabled until a 10-digit number is entered. Navigates to `auth/otp` on success.
- **Footer Links**:
    - **Login**: `Register account` -> Navigates to `auth/signup`.
    - **Signup**: `Already have account` -> Navigates to `auth/login`.

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
