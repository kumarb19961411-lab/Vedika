# Project Structure & Architecture — Vedika V2

This document outlines the organization of the Vedika Android project and the responsibilities of its various modules as of Phase 2B completion.

## 🏛 High-Level Architecture
Vedika follows a multi-module Clean Architecture approach, optimized for modular features and shared core logic.

### 1. `:app` (The Shell)
- **Role**: Entry point and navigation orchestrator.
- **Key Files**:
    - `MainActivity.kt`: Contains the `NavHost`, bottom navigation logic, and shared `AuthViewModel` scoping.
    - `VedikaApplication.kt`: Hilt `@HiltAndroidApp` initialization.
    - `VedikaDestination.kt`: Definition of all application routes and bottom nav items.
- **Dependencies**: Depends on all feature and core modules.

### 2. `:core` (The Foundation)
Shared logic and infrastructure used across the application.
- **`:core:design`**: Premium UI components, `VedikaTheme`, and glassmorphism tokens.
- **`:core:navigation`**: Shared navigation contracts and destination definitions.
- **`:core:data`**: Repositories, API interfaces, and data models (e.g., `VendorRepository`).
    - Contains `VendorMockState` (The continuity contract).

### 3. `:feature` (The Capabilities)
Independent vertical features. Each feature contains its own UI and ViewModels.
- **`:feature:auth`**: Login, Signup, OTP, Category Selection, and Registration forms.
- **`:feature:dashboard`**: Role-aware landing pages (Venue vs. Decorator).
- **`:feature:calendar`**: Booking management and scheduling.
- **`:feature:inventory`**: Partner service management and "Inventory Hub".
- **`:feature:gallery`**: Decorator portfolio and asset showcase.
- **`:feature:finance`**: Transaction tracking (Note: Deprecated for Phase 2B).

---

## 🗺 Navigation & Flow Control

- **Ownership**: The `MainActivity` owns the top-level `NavHost`.
- **State Sharing**: The Auth journey uses an `AuthViewModel` scoped to the `auth_graph` nested navigation entry.
- **Bottom Navigation**: Managed via the `bottomNavItems` list in `:core:navigation`.

## 📦 Dependency Injection
- **Framework**: Hilt.
- **Rule**: Every module must apply `kotlin("kapt")` **before** the Hilt plugin.
- **Rule**: Use `hiltViewModel()` within Composables.

---

## 🔒 Source of Truth Boundaries
- **UI Styling**: Regulated by `core:design`. Avoid ad-hoc color/font hardcoding.
- **Data Logic**: Regulated by `core:data`. ViewModels interact with Repositories (`FakeVendorRepository` for mocks).
