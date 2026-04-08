# Vedika Phase 1: Execution Tasks

## Step A: Data & Domain Layer Setup (`firebase_backend_engineer` + Domain)
- [x] Refactor `FakeAuthRepository` for dummy OTP flow.
- [x] Refactor `FakeBookingRepository` with mock static list flows.
- [x] Refactor `FakeInventoryRepository` with mock catalog flows.
- [x] Refactor `FakeVendorRepository` for vendor properties.

## Step B: Security & Bypass (`security_access_guard`)
- [x] Establish Dev session bypass in `dev` Build Type preventing bleed into `main`.

## Step C: Feature UI Implementation (`android_compose_engineer`)
- [x] **Auth:** Extract Login Gateway (`071014e873224efc839490804317a9a4`).
- [x] **Auth:** Extract OTP Verification (`3708d0cbe5cd42428fdcc3e7b36f178c`).
- [x] **Auth:** Extract Partner Setup (`3dcfdf156a5846fca9d788e8ded247d1`).
- [x] **Dashboard:** Extract Vendor Dashboard (`1ff5ca59143343ef8198104e276f4225`).
- [x] **Dashboard:** Implement Operations `NewBookingScreen`.
- [x] **Calendar:** Extract Calendar Hub (`deb69be0d2014a428025f9c4b8f7f195`).
- [x] **Inventory:** Extract Inventory/Gallery Hub (`2085e094d9da4136842b90995a7eb0bb`).
- [x] **Finance:** Extract Finance Hub (`d1fc42f3ef654617905c7a2477305dbc`).
- [x] **Profile:** Extract Profile Hub (`47d27565caa54e22bc4c49dfe3681e86`).
- [x] Verify `VedikaAppShell` navigation routes map correctly to new screens.

## Step D: MVP Verification (`qa_reliability_engineer`)
- [x] Compile and execute `assembleDevDebug`. to confirm navigation and state emission.
