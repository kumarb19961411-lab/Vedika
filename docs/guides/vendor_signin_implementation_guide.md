# Vendor Sign-In Implementation

## Flow
1. Verify OTP -> Check `vendors/{uid}` exists.
2. Ensure `onboardingComplete` == true.
3. If true, jump to Dashboard. If false, force Onboarding completion.
