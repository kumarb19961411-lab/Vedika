# Vedika Repo Reconciliation & Phase 1 Execution Plan

## 1. Repo Reconciliation Summary (Docs vs. Actual State)

### The Discrepancy
The documentation (specifically `docs/p0task.md` and `docs/p0walkthrough.md`) claims that Phase 1 (Vendor MVP Frontend) is 100% complete. **This is factually incorrect.** The actual repository state reflects a barebones skeleton that has only partially implemented the Phase 0 architecture.

### What Actually Exists
- **Multi-module Architecture:** Correctly set up (`:app`, `:core:design`, `:core:navigation`, `:core:data`, and `:feature:*` modules).
- **Core Navigation Shell:** A functional bottom navigation bar mapping to basic targets exists inside `MainActivity.kt`.
- **Placeholder Screens:** Primitive composables exist for `DashboardScreen`, `CalendarScreen`, `InventoryScreen`, `FinanceScreen`, and `ProfileScreen`.
- **Placeholder Repositories:** Barebones classes like `FakeVendorRepository` and `FakeAuthRepository` exist in the `dev` flavor, but they lack complex emitting logic.
- **Login Screen:** Currently just a "Dev Mode Bypass" button.

### What is Missing (The Delta)
- **Canonical UI:** None of the canonical Stitch screens (Project `12128442228619418215`) are implemented.
- **Auth Flow:** Missing the Login Gateway (`071014...`), OTP Verification (`3708d0...`), and Partner Onboarding (`3dcfdf...`) screens.
- **Business Logic Integration:** No ViewModels successfully emit to the UI using UDF. 
- **Mock Data Layer:** The fake repositories do not generate synthetic, presentation-safe domain data (e.g., booking lists, revenue figures).
- **New Booking Flow:** The `NewBookingScreen` logic and DatePicker UI are absent.

---

## 2. Phase 1 Execution Plan

Based on the actual constraints, we must resume Phase 1 implementation. We will strictly adhere to the agent execution order requested and defer live Firebase network calls. 

### Step A: Data & Domain Layer Setup
**Assignee:** `firebase_backend_engineer` + Domain Engineers
1. Refactor `FakeAuthRepository` to simulate an OTP flow (delay 2s -> yield success) without hitting Firebase.
2. Refactor `FakeBookingRepository` and `FakeInventoryRepository` to emit static `Flow<List<Booking>>` and `Flow<List<InventoryItem>>` states (Loading -> Success).
3. Ensure no live Firebase configuration halts compilation in the `dev` flavor.

### Step B: Feature UI Implementation (Canonical Stitch Screens)
**Assignee:** `android_compose_engineer`
We will systematically extract the UI layers from Stitch Project `12128442228619418215` to replace the placeholders.
1. **Auth Module:** Update `LoginScreen`, add `OtpVerificationScreen` and `PartnerSetupScreen`. 
2. **Dashboard Module:** Implement the real Canonical Dashboard (Revenue card + fast actions). 
3. **Calendar/Inventory/Finance Modules:** Implement their respective lists.
4. **Operations:** Build `NewBookingScreen` with form validation.
*Note: All UI will rely strictly on the `dev` Fake repositories provided via Hilt.*

### Step C: Security & Bypass Guarantees
**Assignee:** `security_access_guard`
1. Enforce that the `dev` bypass mechanism does not bleed into the `main` source set. We will inject a mocked proxy session directly into the `SessionFlow` behind a build variation check.

---

## User Review Required

> [!IMPORTANT]
> **Approval Required:** 
> I have completed the reconciliation audit. The docs falsely reported completion, so we must execute Phase 1 from the barebones state. The plan above outlines the correct execution order (Data/Mocks first -> Compose UI extracted from Stitch second). 
> 
> *Do you approve this updated execution plan to begin constructing the mock repositories and generating the canonical Stitch screens?*
