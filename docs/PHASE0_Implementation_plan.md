# Vedika Vendor App: Phase 0 & 1 Implementation Plan

This plan locks the technical direction for Phase 0 (Architecture Foundation) and Phase 1 (Vendor Frontend MVP) of the Vedika app, incorporating analysis from all domain agents.

## 1. Product Architect Scope & Boundaries

### Phase 0 Summary: Architecture Foundation
- Configure Jetpack Compose, build environments, and dependency injection (Hilt).
- Centralize app shell, modularization structure, and global navigation definitions.
- Implement the "Modern Heritage" Design System (Colors, Typography, Shapes).
- Establish Fake/Mock Repositories to allow fully disjointed frontend execution.

### Phase 1 Summary: Vendor MVP with Mocks
- Build End-to-End Vendor Shell using mocked states.
- Auth Flow: Dev Bypass, Mock OTP, Role/Category Selection.
- Bottom Nav Core: Dashboard, Calendar, Inventory/Gallery, Finance, Profile.
- Supporting Form Flows: New Booking Entry, Inventory Updates, Payout UI.

### Architectural Boundaries
- **Modules (Proposed):** `:app` (shell & DI), `:core:design` (DS tokens/components), `:core:navigation`, `:core:data` (interfaces/models), `:feature:auth`, `:feature:dashboard`, `:feature:calendar`, `:feature:inventory`, `:feature:finance`.
- **State Ownership:** Android Jetpack ViewModels own the UI/Input state. The UI simply reflects the state (UDF). "Source of Truth" for operations relies entirely on injected Repositories (currently Fakes for Phase 1).
- **Deferred to Later Phases:** User-side discovery app, real Firebase/Auth integration, Google Sign-in flow, complex payout/ledger server logic.

---

## 2. Android Compose Engineer: UI & Screen Strategy

### Design System Lock: "Modern Heritage"
Based on PRD and Stitch project `12128442228619418215`.
- **Typography:** Noto Serif for authoritative headers, Plus Jakarta Sans for body/data.
- **Palette:** `#FF9933` (Deep Saffron) Primary, `#006a6a` (Rich Teal) Secondary, Warm cream surface neutrals.
- **Aesthetic Rules:** "No-Line" Rule (use tonal depth/margins instead of hard borders), Glassmorphism headers, `ROUND_EIGHT` border radius for friendly tap targets.

### Reusable Component Inventory
- `VedikaButton`: Primary (saffron), Secondary (gold accent hover), Text/Ghost modes.
- `VedikaTextField`: Pill shape (`rounded-full`), `surface_variant` background, ghost-border.
- `VedikaCard`: `surface_container_lowest` for float effect, no stroke.
- `VedikaStatusChip`: Teal accents for fast operational scanning.
- `VedikaBottomNav`: 5-tab core navigation.

### Bottom Navigation Proposal
1. **Home/Dashboard:** Leads, highlights, earnings snippet.
2. **Calendar:** Availability views and blocked states.
3. **Inventory:** Item management and low-stock highlights.
4. **Finance:** Ledger and payouts.
5. **Profile:** Branding, basic settings.

### Canonical Screen Mapping (Stitch Project Lock)
- **Auth/Onboarding:** `071014e873224efc839490804317a9a4` (Login Gateway), `3708d0cbe5cd42428fdcc3e7b36f178c` (OTP Verification), `3dcfdf156a5846fca9d788e8ded247d1` (Partner Onboarding/Category).
- **Dashboard Hub:** `1ff5ca59143343ef8198104e276f4225` (5-Tab Vendor Dashboard).
- **Calendar Hub:** `deb69be0d2014a428025f9c4b8f7f195` (5-Tab Vendor Calendar).
- **Inventory/Gallery Hub:** `2085e094d9da4136842b90995a7eb0bb` (5-Tab Inventory).
- **Finance Hub:** `d1fc42f3ef654617905c7a2477305dbc` (Payments Details).
- **Profile Hub:** `47d27565caa54e22bc4c49dfe3681e86` (5-Tab Vendor Profile).
- **Operations:** `d0271787a55f4620976fbc3e353e28a3` (New Booking Entry).

> [!TIP]
> **Skipped/Deferred Screens:** All user discovery screens (`be82388723cf44e49a9d806125c942d5`, `e2bb3c6830414027ae881c13d796d2f3`, etc.), iterative unrefined alternate dashboard layouts, and complex variant registrations will be ignored to maintain development velocity on the canonical flow.

---

## 3. Firebase Backend Engineer: Data Strategy

- **Interface-Driven Repositories:** Define `AuthRepository`, `VendorRepository`, `BookingRepository`, `InventoryRepository`. 
- **Phase 1 Mock implementations:** `FakeAuthRepository`, `FakeVendorRepository`. These will emit Rx/Flow primitives with statically seeded dummy lists/states mimicking real network delays (useful for loader UI).
- **Environment Strategy:**
  - `dev` flavor: Hilt provides Fake repositories. Dev-only UI toggle is active.
  - `staging` / `prod` flavors: Hilt will eventually wire real Firebase implementations (Deferred to Phase 2). In Phase 1, these fail-fast or are unconfigured.

---

## 4. QA Reliability Engineer: Test Strategy

- Minimum Phase 0/1 Test Requirements:
  - Compose Preview generation for all base components (DS tokens).
  - UI state mapping validations via JUnit for all ViewModels (asserting Loading -> Success -> Error flows with Fakes).
  - End-to-end local instrumentation flows on the App Shell mimicking login gateway to Dashboard traversal, using Fake Auth.

---

## 5. Security Access Guard: Dev-Bypass Guardrails

- **Hard Rule for Dev Mode:** Dev bypass and fake data seeding MUST be gated via Gradle Build Types/Flavors (e.g. `src/dev/java` vs `src/main/java`). 
- **The "Bypass" mechanism:** The Dev login UI will inject a local "Bypass User Session" straight into the `SessionFlow` rather than mocking JWTs. This strictly prevents accidental leakages of mocked token signers into any staging/production environment. The `DevConfig` object which holds bypass feature flags will be strictly omitted from the `main` or `release` source set.

---

## User Review Required

> [!IMPORTANT]
> **Approval/Block Decision:** As the `product_architect` proxy, this plan correctly structures the boundaries stated in the PRD and locks the canonical Stitch screen IDs. It effectively silos UI work away from backend complexity using Fakes and `dev` flavors.
> 
> *User: Please review the plan above. If you approve of the boundaries, the canonical screen map, module definitions, and mock-first developer-mode strategy, we can proceed to Execution (Phase 0 Setup: Modules and Design System).*
