# Vendor Onboarding Guide

## Flow Description
After a new Vendor verifies their OTP, they must complete their profile before accessing the dashboard.

## Steps
1. **Business Details:** Name, Category, Location.
2. **Verification:** Upload ID/License (Optional in Phase 2).
3. **Commit:** Data is written to `vendors/{uid}` with `onboardingComplete = true`.

## Navigation Exit
Once `onboardingComplete` sets to true, the observer triggers navigation to `VendorShell`.

## Post-Onboarding Consumption
After onboarding, all vendor screens (Dashboard, Profile, Settings) strictly consume the canonical Firestore document via `vendorRepository.getVendorProfileStream(uid)`.
- **Dashboard**: Streams business metadata and stats.
- **Profile**: Displays the "Partner Command" view with full persistence.
- **Graceful Defaults**: Screens implement standard fallbacks (e.g., "—") for any non-mandatory profile fields to ensure UI stability.

---
[[Project_Hub|🏠 Project Hub]] | [[Vendor_Flow_Hub|🏢 Vendor Flow Hub]] | [[vendor_onboarding_guide|Top]]
