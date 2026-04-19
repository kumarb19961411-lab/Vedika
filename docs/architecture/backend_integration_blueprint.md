# Backend Integration Blueprint

## Phase 3 Integration Scope
The Vedika client utilizes a "local-first UI, remote-second data" architecture. This document maps out how the existing Jetpack Compose layer will hook into our backend infrastructure, specifically focusing on Firebase.

## 1. Core Services Setup
### Firebase Authentication
- Configured strictly for `PhoneAuthProvider` to handle Indian mobile numbers primarily.
- Custom Backend Claims (via cloud functions in future phases) will solidify User vs Vendor demarcations, but presently relies exclusively on Document ID targeting.

### Cloud Firestore
- Operates under strict separation of data planes.
- **Plane A (Vendors):** `vendors/{uid}` contains heavy read-load structures for discovery elements.
- **Plane B (Users):** `users/{uid}` contains private identifiable information restricted completely to self-query.

### Emulator Suite
- Native compilation points to `localhost` Firebase Emulator for local developer testing to ensure CI automation doesn't pollute live databases. `data/local` points directly back to emulator host `10.0.2.2`.

## 2. Synchronization Policies
- All writes from the client are opportunistic. We update the UI optimistic cache instantly and trigger a Firestore `.set()` block in coroutine scopes.
- All reads from the client are primarily powered via Snapshot Listeners attaching to active screen ViewModels. Thus, if a booking confirms on the vendor console, the user app reflects the state shift instantaneously without required pull-to-refresh.

## 3. Deployment Safety Protocol
- Before any push to `production`, `firestore.rules` must be verified via the test console.
- Default locked state: `allow read, write: if false;` applies to all collections not explicitly overridden in access models.
