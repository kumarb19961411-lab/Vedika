# Version 2 Phase 1 Screen Map

This document tracks the mapping between the locked source folders in `resources/phase1` and the corresponding Jetpack Compose implementation files.

## Phase 1 Locked Mapping

| Source Folder Name | Target Kotlin/Compose Screen | Navigation Route | Type | Bottom Nav | Action |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `kalyanavedika_interactive_login_gateway` | `LoginScreen.kt` | `auth/login` | Auth | Hidden | MODIFY |
| `kalyanavedika_otp_verification` | `OtpVerificationScreen.kt` | `auth/otp` | Auth | Hidden | MODIFY |
| `kalyanavedika_category_selection_no_nav` | `CategorySelectionScreen.kt` | `registration/category` | Onboarding | Hidden | CREATE |
| `venue_registration_refined_flow` | `VenueRegistrationScreen.kt` | `registration/venue` | Registration | Hidden | CREATE |
| `decorator_registration_expanded_expertise` | `DecoratorRegistrationScreen.kt` | `registration/decorator` | Registration | Allowed | CREATE |

## Do Not Touch In This Pass

The following areas are strictly out of scope for UI modifications during Phase 1. Any changes here will be rejected by the Fidelity Guard.

- **Dashboard**: `DashboardScreen.kt`, `NewBookingScreen.kt`
- **Calendar**: `CalendarScreen.kt`
- **Finance**: `FinanceScreen.kt`
- **Inventory**: `InventoryScreen.kt`
- **Profile**: `ProfileScreen.kt`
- **Navigation Scaffolding**: Changes to `MainActivity.kt` should be limited to route registration only.
- **Styling**: Do not modify `VedikaTheme` or core design tokens unless a missing Phase 1 token is identified in the source pack.

## Implementation Notes

- **PartnerSetupScreen Refactor**: The existing `PartnerSetupScreen.kt` is deprecated as a primary destination. Its logic (e.g., category selection, venue details) should be extracted into the new separate Phase 1 screens or kept as internal shared components if needed.
- **Route Registration**: New routes must be added to `VedikaDestination` or the equivalent navigation contract.
