# Android Engineering Guardrails

## Purpose
This is the workspace-wide Android engineering rulebook. It defines non-negotiable architecture and implementation guardrails for the entire Vedika app.

## Scope
These rules apply to all Android modules, feature development, UI implementation, and architectural structures across the Vedika workspace.

## Mandatory Standards
- **Kotlin-First:** All Android development must strictly execute in Kotlin.
- **Jetpack Compose First:** New UI screens and components must be built exclusively using Jetpack Compose.
- **MVVM Architecture:** Strict adherence to the Model-View-ViewModel paradigm is required.
- **Clean Architecture Layering:** Maintain absolute separation across UI, Domain, and Data layers.
- **Dependency Injection:** Hilt is the mandatory dependency injection framework.
- **Unidirectional Data Flow (UDF):** State flows down; events flow up.
- **Testability & Maintainability:** Code must be constructed with testability and long-term production maintenance as primary constraints.

## Layering Expectations
- **UI Layer:** Owns UI rendering and state display. Strictly prohibited from harboring business or validation logic.
- **Domain Layer:** Owns core business rules and stateless UseCases. Holds zero knowledge of UI or Data implementation specifics.
- **Data Layer:** Owns Repositories, API requests, and data source coordination. Abstracts all Firebase operations away from the rest of the application.

## UI Rules
- **Thin Entry Points:** Activities and Fragments must act solely as thin entry points or navigational hosts.
- **Stateless/Stateful Segregation:** Composables must cleanly separate state rendering from state generation. They emit events and consume state but do not own business rules.
- **Reusable Over Copy-Paste:** Shared UI elements must be consolidated into reusable core UI/design system components. Component sprawl is banned.

## ViewModel/State Ownership Rules
- ViewModels definitively orchestrate and own presentation state.
- ViewModels never execute direct backend chaos or complex data manipulation without deferring to the Domain/Data layers.
- State emission must be reactive and immutable.

## Data Boundary Rules
- **No Direct UI-to-Firebase Coupling:** Firebase Firestore, Storage, or Auth elements must NEVER appear in Composable or UI configuration files.
- **No Direct Composable-to-Firestore Logic:** Composables cannot observe Firestore snapshot streams directly. Streams must map to agnostic domain models within the Data/Domain layer before hitting the UI.
- All Firebase interactions remain encapsulated firmly behind Repository interface boundaries.

## Feature Module Expectations
- Modular design must be utilized to encapsulate specific feature domains and core sharable elements.
- Navigation boundaries must remain pristine. Feature modules must remain loosely coupled, routing strictly through canonical navigation definitions.

## Coding and Maintainability Expectations
- Long-term maintainability supersedes short-term convenience.
- Weak “MVP shortcut” arguments are unacceptable if they induce compounding structural debt.
- New features are completely prohibited from bypassing the established architecture for the sake of speed.

## Review Expectations
- Feature implementations will be rejected if they breach layering, leak Firebase boundaries, or exhibit untestable architectures.

## Non-Negotiable Constraints
- **NO** business logic in Activity, Fragment, or Composable files.
- **NO** direct mutation of backend state initiated from UI screen code.
- **NO** overlapping or undocumented navigation gateways between decoupled modules.

## Anti-Patterns To Reject
Crush the following anti-patterns instantaneously:
- Giant, monolithic screen files.
- God ViewModels containing thousands of lines spanning multiple disconnected concerns.
- Any calculation of business validation logic living natively inside a Composable.
- Firestore network calls or listeners declared inside UI functions.
- Direct mutation of backend data states triggered randomly from screen code instances.
- Duplicated business logic copy-pasted across multiple screens.
- Overcoupled feature modules defeating the purpose of modular architecture.
- Weak, ambiguous variable naming and unclear state ownership.
- Hacky, one-off "quick" implementations deceptively pitched as final architecture.
