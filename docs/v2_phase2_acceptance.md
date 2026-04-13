# Phase 2 Acceptance Criteria — Vedika V2

This document defines the "Definition of Done" for Phase 2 implementation, focusing on Phase 2A shell rules and Phase 2B context preparations.

## 🏁 Phase 2A: Shell & Registration Alignment

### 1. Navigation & Shell Behavior
- [x] **Registration Isolation**: All screens under the `registration/` route path MUST NOT show the `BottomNavigationBar`.
- [x] **Decorator Route Correction**: The old special-case for `DecoratorRegistration.route` in `MainActivity.kt` MUST be deleted.
- [x] **Close Behavior**: Registration screens must feature a 'Close' (X) or 'Back' button that returns the user to the `CategorySelection` or `OtpVerification` as appropriate.
- [x] **Shell Entry**: The `BottomNavigationBar` must only become visible after successful navigation to a dashboard route (e.g., `dashboard/venue`).

### 2. UI Fidelity
- [x] **Source Alignment**: `CategorySelection`, `VenueRegistration`, and `DecoratorRegistration` must match the exports in `resources/phase1` exactly (pixel, typography, padding).
- [x] **Modern Heritage Styling**: Use orange accent `#EA580C` for primary buttons and `MaterialTheme.typography` for all labels.

### 3. Navigation Continuity
- [x] **Handoff Logic**: Navigating from a finished registration screen to the dashboard must use `popUpTo(AuthGraph) { inclusive = true }` to prevent back-navigation into registration.

## 🏁 Phase 2B (Future) Context Prep

### 4. Data Continuity
- [x] **Mock Data Hook**: Successful registration input must populate a local `FakeVendorState` that is immediately readable by the incoming `DashboardScreen`.
- [x] **Contract Consistency**: The fields captured in `VenueRegistration` must map 1:1 to the fields displayed in the `VenueDashboard` command center.

## 🔍 Validation Checklist
1. **Registration Flow**: SignUp -> OTP -> Category -> Form -> Dashboard.
2. **Backstack**: Back button on dashboard must not go back to registration.
3. **Bottom Nav**: Present on Dashboard/Profile/Inventory; Absent on Signup/OTP/Registration.
