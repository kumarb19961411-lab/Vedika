# Changelog — Vedika V2

A record of significant architectural and feature milestones in the Vedika V2 project.

## [2026-04-10] — Phase 1 Interactivity Complete
### Added
- **Dedicated Signup Screen**: Separated Registration entry from Login entry to support different role intents.
- **Shared State Architecture**: `AuthViewModel` now tracks `selectedCategory` and `authFlow` across transitions.
- **Form Validation**: Reactive button states on Venue and Decorator registration screens.
- **Micro-interactions**: Added Snackbar feedback for "Save Draft" and "Registration Success".
- **Documentation Overhaul**: Created central README, Project Structure map, and Emulator Checklist.

### Fixed
- Navigation "accidental route flip" from Login to Registration.
- Numeric keyboard behavior for capacity and pricing fields.
- Fixed `NavHost` scoping to ensure ViewModel instances are correctly shared.

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
