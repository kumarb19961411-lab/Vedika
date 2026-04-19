# Repository Map

The Vedika project uses a multi-module architecture:
- `app`: Application shell, DI wiring, environment constants.
- `feature:auth`: All onboarding, sign in, sign up, and OTP flows.
- `feature:dashboard`: User and Vendor main interfaces.
- `core:data`: Firebase repositories, EncryptedSharedPreferences.
- `core:ui`: Theme, common composables.
