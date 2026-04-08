# Vedika Vendor App Implementation Tasks

## Phase 0: Architecture Foundation ✅
- [x] Configure Build Environment
  - [x] Implement multi-module `settings.gradle.kts`
  - [x] Configure Build Flavors (`dev`, `staging`, `prod`) in `:app`
  - [x] Integrate Compose, Hilt, Navigation and Kotlin 2.0
- [x] Design System Implementation (`:core:design`)
  - [x] Colors & Tokens (Modern Heritage)
  - [x] Typography & Shapes
  - [x] Core Compose Setup
- [x] **Data layer:** Define repository interfaces in `:core:data`.
- [x] **DI Setup:** Setup Hilt in `:app` and create Fake implementation bindings for the `dev` flavor.

## Phase 1: App Shell & Fake Navigation ✅
- [x] Implement robust `MainApplication.kt` shell.
- [x] Draft canonical Navigation Graph (Dashboard, Calendar, Inventory, Finance, Profile, NewBooking).
- [x] Implement `LoginScreen` with dev-bypass logic.
- [x] Setup 5-tab Bottom Navigation Shell with Hilt-injected fake repositories.
- [x] Implement Dashboard UI (revenue card, stats, upcoming bookings list via `DashboardViewModel`).
- [x] Implement Calendar UI (all bookings with confirm/decline actions via `CalendarViewModel`).
- [x] Implement Inventory UI (items with availability toggle via `InventoryViewModel`).
- [x] Implement Finance UI (revenue breakdown + transaction list via `FinanceViewModel`).
- [x] Implement Profile UI (vendor info, dev badge, logout via `ProfileViewModel`).
- [x] Add New Booking Entry form (`NewBookingScreen` + `NewBookingViewModel` with DatePicker, validation, animated preview card, and FAB entry point).
  - [x] Added `createBooking()` to `BookingRepository` interface & `FakeBookingRepository`
  - [x] Fixed JDK conflict — pinned `org.gradle.java.home` to JDK 17 in `gradle.properties`

## Phase 2: Firebase Integration
- [x] Register App & Setup Gradle Config (`google-services.json`, `firebase-bom`)
- [ ] Connect Firebase Authentication (Phone/OTP flow)
- [ ] Replace Fake repositories with Firebase/Firestore real implementations
- [ ] Implement Firestore security rules
- [ ] Add Cloud Functions for server-side trust operations
- [ ] Staging & Production build configuration
