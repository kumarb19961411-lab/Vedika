# Session Restoration Strategy

## Goal
Achieve 0-latency perceived login for returning users.

## Mechanism
1. **Local State:** `EncryptedSharedPreferences` stores `accessToken`, `refreshToken`, `uid`, and `role`.
2. **Launch:** `SplashViewModel` validates local state.
3. **Route:** If valid, `popUpTo(0)` navigates to the respective Dashboard.
4. **Renewal:** If expired, attempt transparent refresh. If failed, force re-login.
