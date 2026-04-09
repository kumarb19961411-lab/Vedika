# Version 2 Phase 1 Acceptance Checklist

This document tracks the signoff for the 5 locked screens to ensure exact UI fidelity before final integration.

## Global Acceptance Points
- [x] **Bottom Navigation Policy**: Correctly hidden for 4 screens; **explicitly visible** for the Decorator Registration screen as per source.
- [x] **Route Flow**: Navigation flow works seamlessly: Login -> OTP -> Category -> (Venue/Decorator) -> Dashboard.
- [x] **Clean Navigation**: Top app bar / Back behavior matches source exports (Check back behavior).
- [x] **Architecture**: Zero backend logic or Firebase calls added to Compose UI.
- [x] **Dev Mode**: "Bypass" or "Mock" behavior preserved for local testing.

## Per-Screen Fidelity Checkpoints

### 1. Interactive Login Gateway (`auth/login`)
- [x] Layout matches `kalyanavedika_interactive_login_gateway` source.
- [x] Typography hierarchy (Headline vs Body) is exact.
- [x] Background imagery/gradient matches source.
- [x] Login button placement and styling are correct.

### 2. OTP Verification (`auth/otp`)
- [x] Layout matches `kalyanavedika_otp_verification` source.
- [x] OTP field spacing and interaction (autofocus/keyboard) are smooth.
- [x] Timer/Resend UI matches source exactly.

### 3. Category Selection (`registration/category`)
- [x] Layout matches `kalyanavedika_category_selection_no_nav` source.
- [x] Card grouping for categories matches source imagery and spacing.
- [x] Single/Multiple selection logic matches visual feedback in source.
- [x] **Bento Grid**: Grayscale/Disabled states for secondary categories match Stone palette.

### 4. Venue Registration (`registration/venue`)
- [x] Layout matches `venue_registration_refined_flow` source.
- [x] Form field order and labels match exports exactly.
- [x] Section headers and grouping (e.g., Contact vs Venue Info) match.

### 5. Decorator Registration (`registration/decorator`)
- [x] Layout matches `decorator_registration_expanded_expertise` source.
- [x] **Bottom Navigation**: Correctly rendered in the mobile view as per source `code.html`.
- [x] **Expertise States**: Exactly two categories are pre-selected/enabled, with others disabled as shown in the source.
- [x] Expertise selection (chips/checkboxes) matches source layout.
- [x] Portfolio/Work history fields match source placement.

## Technical Signoff
- [ ] **Compile Success**: The app builds without errors.
- [ ] **Lint Check**: No UI-related lint warnings in the new screens.
- [ ] **Screenshot Comparison**: Side-by-side screenshot verification against `resources/phase1` exports performed for all 5 screens.
- [ ] **Final Signoff**: Human operator review completed.
