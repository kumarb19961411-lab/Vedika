# Session Restoration Guide

## Local Persistence
Save `token` via EncryptedSharedPreferences on first load.
Check this on `SplashViewModel` load to determine immediate routing to dashboard, bypassing OTP entry altogether for active sessions.
