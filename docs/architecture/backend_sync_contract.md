# Backend Sync Contract

## Principle
Vedika uses local-first cache for UI speed, backed by Firestore.

## Collections & Schemas

### `vendors/{uid}`
- `businessName` (String)
- `category` (String)
- `onboardingComplete` (Boolean)

### `users/{uid}`
- `name` (String)
- `phone` (String)

## Sync Behavior
Profile updates are written directly and trigger a snapshot listener refresh. bookings/leads flow identically.
