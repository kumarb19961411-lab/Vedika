# Changelog — Vedika V2

A record of significant architectural and feature milestones in the Vedika V2 project.
6: 
7: ## [2026-04-14] — Phase 2B: High-Fidelity Vendor Shell & Data Continuity
### Added
- **Data Continuity Binding (Run C)**:
    - **Personalization**: Added `Full Name` field to Signup to drive `ownerName` across the dashboard and profile.
    - **High-Fidelity Mock Mapping**: Extended `VendorMockState` with `yearsExperience` and `packageTiers` (typed model).
    - **Role-Aware Dashboards**: Fully bound registration data (Venue name, capacity, pricing, amenities, experience, and tiers) to the специализированные dashboards.
    - **Live Profile**: Profile screen now reflects real user name, business name, and location from the onboarding funnel.
    - **Data Contract**: Created `docs/v2_phase2_data_contract.md` to formally document the mock data lifecycle.
- **Verification & Acceptance (Run D)**:
    - **Final Audit**: Verified all 6 Phase 2B screens for pixel fidelity and role-appropriate content.
    - **Acceptance Locked**: Updated `docs/v2_phase2_acceptance.md` and created formal verification report.
    - **Regression Guard**: Re-verified Hilt, Kapt, and Navigation scoping after continuity changes.
- **Refinement & Polish Pass (Run E)**:
    - **Shell Standardization**: Created `VedikaTopAppBar` for a consistent, role-aware top bar (NotoSerif typography, heritage branding) across all root vendor screens.
    - **Navigation Safety**: Enforced zero-back-arrow policy on all 5 root tab screens (Dashboard, Calendar, Gallery, Inventory, Profile).
    - **Role-Aware Gallery**: Refactored `GalleryScreen` to dynamically load content and styling for Venues vs. Decorators.
    - **Role-Aware Inventory**: Refactored `InventoryScreen` with seeded demo data specific to the vendor type.
    - **Finance Cleanup**: Removed finance-centric cards from the `ProfileScreen` bento grid to align with Phase 2B scope.
    - **Interactive Calendar**: Wired the calendar "Add" action to a snackbar-driven mock interaction.
    - **Data Continuity**: Verified and bound registration data (Full Name, Business Name, Location) across all refined screens.

### Fixed
- **Build Stability (Run F)**: 
    - Resolved critical brace nesting errors in `VenueDashboardScreen` and `DecoratorDashboardScreen` following UI wrapping.
    - Fixed unresolved reference 'id' in `InventoryViewModel` by standardizing on a consistent mock vendor key.
    - Verified full application build via `./gradlew assembleDebug` (Build Successful).
- **UI Syntax Guard**: Resolved invalid `RoundedCornerShape(full = 999.dp)` and `ContentScale` errors across the dashboard module.
- **Dependency Guard**: Added missing `coil-compose` to the inventory module to resolve image rendering issues.

## [2026-04-14] — Phase 2A: Registration Shell & Data Contract Locked
8: ### Added
9: - **Shell Isolation**: Fully isolated the registration flow by removing the Bottom Navigation Bar from all screens within the `auth_graph`.
10: - **Navigation Refinement**: Implemented `popUpTo(AuthGraph) { inclusive = true }` for registration completion to prevent back-navigation into completed forms.
11: - **UI Fidelity Pass**: Refined `VenueRegistrationScreen` and `DecoratorRegistrationScreen` to match Phase 1 pixel-perfect designs, including dash-border portfolio placeholders.
12: - **Data Continuity Contract**: Established `VendorMockState` and `VendorRepository` to ensure data persists from registration to the vendor dashboard.
13: - **State Hoisting**: Moved registration form state into `AuthViewModel`, enabling data preservation during navigation within the onboarding funnel.
14: 
15: ### Fixed
16: - **Decorator Route Correction**: Removed legacy special-case logic that showed the bottom bar on the decorator registration route.
17: - **Navigation Icons**: Standardized `ArrowBack` behavior across all registration-related screens.

## [2026-04-10] — Phase 1 Interactivity Complete
### Added
- **Dedicated Signup Screen**: Separated Registration entry from Login entry to support different role intents.
- **Shared State Architecture**: `AuthViewModel` now tracks `selectedCategory` and `authFlow` across transitions.
- **Form Validation**: Reactive button states on Venue and Decorator registration screens.
- **Micro-interactions**: Added Snackbar feedback for "Save Draft" and "Registration Success".
- **Role-Based Logic Lock**: Fixed deterministic routing for User vs Partner roles. Users now correctly bypass registration regardless of flow.
- **State Stability & Copy Pass**: Locked exact product copy and role-helper text across Sign In/Sign Up. Ensured `AuthFlow` and `AccountMode` are strictly preserved across steps.
- **Product Alignment**: Renamed legacy `LoginMode` to `AccountMode` across the auth module.
- **Partner Branching**: Implemented deterministic routing from Category Selection into specialized Venue and Decorator registration forms.
- **Verification & Safety Lock**: Standardized all 5 post-OTP routing paths and established a formal Build Regression Guard.
- **Documentation Overhaul**: Created central README, Project Structure map, and Emulator Checklist.

### Fixed
- Navigation "accidental route flip" from Login to Registration.
- Numeric keyboard behavior for capacity and pricing fields.
- **Auth Graph Scoping**: Implemented nested navigation graph (`auth_graph`) to scope a single `AuthViewModel` instance across the entire onboarding funnel. Fixes the state-loss bug during the Partner Sign Up flow.
- **Verification & Safety Lock**: Standardized all 5 post-OTP routing paths and established a formal Build Regression Guard.

---

## [2026-04-09] — Auth Foundation & Build Stability
### Added
- **OTP Verification**: Manual input, verification logic, and "Change Number" support.
- **Category Selection**: Bento-style interactive cards for Vendor role selection.
- **Build Regression Guard**: Established a "Golden Path" for Firebase and Hilt stability.

### Fixed
- Fixed critical Hilt-Kapt plugin ordering issues across all modules.
- Resolved Firebase `applicationIdSuffix` mismatch.
- Standardized toolchain on Java 17 and AGP 8.7.2.

---

## [2026-04-08] — Project Initialization
- Initial module setup for `feature:auth`.
- Migration of base `VedikaTheme` and glassmorphism tokens.
