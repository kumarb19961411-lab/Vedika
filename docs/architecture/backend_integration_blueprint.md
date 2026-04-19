# Backend Integration Blueprint

## Phase 3 Scope
Integrates the local client state with remote Firebase services.

## Services
1. **Firebase Auth**: Connects OTP `PhoneAuthProvider`. Custom claims to resolve roles.
2. **Firestore**: Two isolated collections `users` and `vendors`.
3. **Emulator**: Local host test environment with populated mock data.
