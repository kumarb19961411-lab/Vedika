# Vedika V2 Phase 1 Interaction Contract

This document outlines the state-wiring and interaction behavior for the Phase 1 onboarding flow (Run E).

## 1. Login Gateway
- **Mode Toggle**: Persists `LoginMode` (USER/VENDOR) in `AuthViewModel`.
- **Phone Input**: 10-digit validation.
- **Action**: "Send OTP" triggers a mock network call with a 1-second delay.
- **Error Handling**: Displays inline validation errors for invalid phone numbers or simulated network failures.

## 2. OTP Verification
- **Input**: 1-digit restricted fields (4 total).
- **Auto-Focus**: (Future improvement) Cursor moves to next field on entry.
- **Countdown**: 30-second timer for "Resend OTP".
- **Validation**: 
  - `1234`: Simulates a NEW partner (navigates to Category Selection).
  - `0000`: Simulates an EXISTING partner (navigates to Dashboard).
  - Others: Displays "Invalid OTP" error.

## 3. Category Selection
- **Selection**: Radio-style selection between "Heritage Venue" and "Heritage Decorator".
- **Validation**: "Proceed" button enabled only when a category is selected.

## 4. Venue Registration
- **Identity**: Interactive "Venue Identity" and "Guest Capacity" fields.
- **Location**: Editable address field with decorative map icon.
- **Amenities**: Multi-select chips for standard heritage venue features.
- **Pricing**: Functional base price input with currency symbol prefix.

## 5. Decorator Registration
- **Identity**: Business name and years of experience (fixed dropdown).
- **Expertise**: Multi-select cards for specialized service categories.
- **Packages**:
  - Essential: Editable starting price.
  - Premium: Editable price and inclusion list (large text area).

## System State (Mock Mode)
- **Persistence**: All registration data is temporary and held in-memory or in the `AuthViewModel` state for the session.
- **Developer Bypass**: "Developer Route (Bypass)" in LoginScreen remains functional for rapid testing of subsequent phases.
