# Project Structure & Architecture — Vedika V2

This document outlines the organization of the Vedika Android project and the responsibilities of its various modules.

## 🏛 High-Level Architecture
Vedika follows a multi-module Clean Architecture approach, optimized for modular features and shared core logic.

### 1. `:app` (The Shell)
- **Role**: Entry point and navigation orchestrator.
- **Key Files**:
    - `MainActivity.kt`: Contains the `NavHost` and shared `AuthViewModel` scoping.
    - `VedikaApplication.kt`: Hilt `@HiltAndroidApp` initialization.
- **Dependencies**: Depends on all feature and core modules.

### 2. `:core` (The Foundation)
Shared logic and infrastructure used across the entire application.
- **`:core:common`**: Utilities, extensions, and base classes.
- **`:core:designsystem`**: Premium UI components, `VedikaTheme`, and glassmorphism tokens.
- **`:core:data`**: Repositories, API interfaces, and data models (e.g., `AuthRepository`).
- **`:core:model`**: Pure data entities (Venue, Partner, User).

### 3. `:feature` (The Capabilities)
Independent vertical features. Each feature contains its own UI and ViewModels.
- **`:feature:auth`**: Sign In, Sign Up, OTP, and Onboarding registration screens.
- **`:feature:dashboard`**: User/Partner landing pages (Future V2 focus).
- **`:feature:inventory`**: Partner service management (Future V2 focus).

---

## 🗺 Navigation & Flow Control

- **Ownership**: The `MainActivity` owns the top-level `NavHost`.
- **State Sharing**: Cross-screen state (like the auth journey) is handled by a `AuthViewModel` scoped to the `NavHost` closure in `MainActivity`.
- **Destinations**: Defined in a centralized `VedikaDestination` enum or sealed class within the app module.

## 📦 Dependency Injection
- **Framework**: Hilt.
- **Rule**: Every module using Hilt must apply the `kotlin("kapt")` plugin **before** the Hilt plugin to ensure correct stub generation.
- **Rule**: Use `hiltViewModel()` within Composables to ensure lifecycle-aware injection.

---

## 🔒 Source of Truth Boundaries
- **UI Styling**: Regulated by `core:designsystem`. Avoid ad-hoc color/font hardcoding.
- **Data Logic**: Regulated by `core:data`. ViewModels should only interact with Repositories, never directly with Firebase/Mocks.
