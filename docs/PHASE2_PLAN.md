# Vedika Phase 2: Firebase Integration & Architecture Realization

## Objective
Execute Phase 2 in a controlled, architecture-safe way by introducing Firebase foundation, live repository skeletons, environment-aware switching, and opt-in emulator-friendly setup behind the existing Phase 0/1 vendor app shell.

## Scope In
- **Firebase Skeletons**: Implement `FirebaseAuthRepository`, `FirebaseVendorRepository`, `FirebaseBookingRepository`, `FirebaseInventoryRepository` matching current *exact* `core:data` interfaces.
- **Environment Targeting**: Dev (fakes/mock), Staging (live/test backend), Prod (live backend).
- **Emulator Support**: Add a config/dev-toggle to point `staging` or `dev` to `10.0.2.2` Firebase Emulators when requested.
- **Data Collections Mapping**: Basic Firestore mapping for vendors, bookings, inventory.

## Scope Out
- Production auth shortcuts (e.g. Google Sign-In, Apple).
- Overbuilding finance/ledger repositories (Finance is derived directly from Bookings).
- Client-authoritative claims for integrity-sensitive flows.
- Modifying Compose screens for business logic (Logic STAYS in DI/Repository).

## Implementation Order
1. **product_architect**: Repo audit & Doc Bootstrap (Current)
2. **firebase_backend_engineer**: Live Firebase Repository Skeletons & Emulator Routing
3. **security_access_guard**: Security Rules & Safe Bypasses
4. **booking_inventory_rules_engineer**: Booking/Inventory Boundaries definition
5. **finance_ledger_engineer**: Safe Finance computations definition
6. **android_compose_engineer**: UI/DI Wiring checks (None expected if DI is clean)
7. **qa_reliability_engineer**: Compilation and environment validation

## Risks & Assumptions
- *Assumption*: Fake views rely ONLY on the abstract interfaces defined in `Repositories.kt`.
- *Risk*: Firestore local emulation limits might conflict with Auth configurations without proper scoping.
