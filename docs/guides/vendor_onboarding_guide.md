# Vendor Onboarding Guide

## Flow Description
After a new Vendor verifies their OTP, they must complete their profile before accessing the dashboard.

## Steps
1. **Business Details:** Name, Category, Location.
2. **Verification:** Upload ID/License (Optional in Phase 2).
3. **Commit:** Data is written to `vendors/{uid}` with `onboardingComplete = true`.

## Navigation Exit
Once `onboardingComplete` sets to true, the observer triggers navigation to `VendorShell`.
