# Vedika App: Phase 0 & 1 Walkthrough

This document outlines the foundation built during **Phase 0** and the frontend MVP components and logic implemented during **Phase 1** of the Vedika Vendor app. 

Our core philosophy was to establish a highly modular, decoupled architecture where the UI runs independently from the backend using mocked "Fake" repositories.

---

## 🏗️ Phase 0: Architecture Foundation

Phase 0 established the rigid application architecture, ensuring we don't encounter structural or routing bottlenecks as the app scales.

### Multi-Module Structure
We divided the project into highly cohesive segments to optimize build times and enforce separation of concerns:
- **`:app`**: Contains dependency injection (DI) wiring via Hilt, app flavors, and the main application shell entry points.
- **`:core:design`**: Houses our proprietary "Modern Heritage" design system (colors, typography, shapes).
- **`:core:navigation`**: Defines safe and structured route objects for navigating between features.
- **`:core:data`**: Declares our abstract repository interfaces (e.g., `AuthRepository`, `BookingRepository`) and our core data models without implementing concrete functionality.
- **Feature Modules** (`:feature:auth`, `:feature:dashboard`, etc.): Standalone slices of the app containing `Screen` UI layouts and `ViewModels` corresponding to isolated parts of the app.

### Build Flavors & DI Strategy
We implemented three build flavors: `dev`, `staging`, and `prod`. 
During these initial phases, we relied exclusively on the `dev` flavor. This allowed us to inject **Fake Repositories** directly into our `ViewModels` using Hilt, allowing the UI to exist with realistic states (loading, error, success) while completely offline.

### Design System: Modern Heritage
Using the Figma designs as inspiration, we constructed the core Material 3 theme:
- **Typography:** `Noto Serif` for elegant headings and `Plus Jakarta Sans` for clean, readable secondary bodies.
- **Color Palette:** Saffron primary themes juxtaposed with rich Teal secondary elements inside a warm surface container scheme.

---

## 📱 Phase 1: App Shell & Frontend MVP

With the architecture set, Phase 1 focused on building the canonical application shell and executing all core vendor screens using the mock data.

### 1. App Shell and Navigation Structure
We implemented `VedikaAppShell` inside the `MainActivity`. This sits above all other composables and manages our `NavHost`.
- We integrated horizontal slide animations between our navigation components for a more fluid interaction.
- The `NavigationBar` (Bottom Nav Toolbar) is highly aware of the current root routing, displaying its 5 tabs contextually.

### 2. Login Gateway & Dev Bypass
We built a completely functional `LoginScreen` structure. Because we were in the `dev` flavor, we introduced a "Dev Bypass" allowing instantaneous sign-in flow logic (simulating the sending and verifying of an OTP) to immediately fetch a mocked vendor JWT proxy. 

### 3. Core Feature Implementations
We established the primary 5-tab workspace. Every core feature follows Jetpack Compose Unidirectional Data Flow (UDF) standards via a `ViewModel` providing a `StateFlow`.
* **Dashboard Hub:** A revenue summary card dynamically calculating total earnings alongside quick-action metrics displaying confirmed vs. pending bookings, rounding off with a quick list of upcoming reservations.
* **Calendar:** An expanded booking list providing actionable standard buttons (Confirm/Decline) mapped out natively within the `CalendarViewModel`.
* **Inventory:** A vendor catalog rendering availability toggle switches.
* **Finance:** A summarized breakdown comparing gross margins against platform fees along with scrollable transaction statements.
* **Profile:** A vendor overview granting options to cleanly Sign Out and reset the global state/navigation graph safely.

### 4. New Booking Transaction Flow
Finally, we hooked up the "+ New Booking" action. 
* Integrated an `ExtendedFloatingActionButton` on the dashboard layout.
* Created the `NewBookingScreen` incorporating local string validations, a responsive custom Material 3 `DatePickerDialog`, and animated elements. 
* Linked form action submissions down through our domain into the `FakeBookingRepository.createBooking()` which natively returns and seeds the new state, immediately updating the main Dashboard metrics upon navigating back. 

---

## 🎯 Verification & Stability
* We resolved all unresolved reference dependencies (such as missing `compose.material.icons.extended` libraries).
* Cleaned up build errors by forcing our build daemon environment specifically to `JDK 17` inside `gradle.properties`.
* Verified that `assembleDevDebug` compiles flawlessly offline.

**Status Map:** Both Phase 0 and 1 are 100% complete and fully pushed to the `main` branch. 

> [!NOTE] 
> This fully mocked baseline ensures that our UI requires strictly zero adjustments. In **Phase 2**, as we integrate Firebase Firestore schemas, our ONLY code changes should theoretically occur behind the scenes inside our repository domain layer by redirecting Hilt routing inside the `:app` module!
