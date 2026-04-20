# Emulator & Manual Verification Checklist — Vedika V2

Use this checklist to verify the stability of the Vedika V2 Golden Path on a fresh emulator or physical device.

## 🏁 Phase 2B Verification Pass

### 1. The Partner Onboarding Journey
- [ ] **Signup**: Enter a NEW phone number and unique **Full Name**.
- [ ] **OTP**: Verify using `123456` (Dev Mode).
- [ ] **Category**: Select "Venues" card.
- [ ] **Registration**: Enter unique "Venue Name" and location.
- [ ] **Completion**: Click "Complete Registration".
- [ ] **Handoff**: Verify Dashboard shows correctly:
    - [ ] `NAMASTE, {FullName entered}`
    - [ ] `{VenueName entered}` as business title.
    - [ ] Correct daily price and guest capacity.

### 2. The Vendor Shell & Navigation
- [ ] **Tabs**: Navigate through all 5 tabs:
    - [ ] Dashboard (Visual check)
    - [ ] Calendar (Grid check)
    - [ ] Gallery (Portfolio check)
    - [ ] Inventory (Asset check)
    - [ ] Profile (Bento grid check)
- [ ] **Visibility Rules**:
    - [ ] Navigate to **Profile**.
    - [ ] Bottom bar remains visible.
    - [ ] Navigate back to **Registration** (Should be impossible via UI).
- [ ] **Backstack Integrity**: On the Dashboard, press the system Back button.
    - [ ] **Success**: App should minimize/exit, NOT go back to the registration form.

### 3. Bento Profile Operations
- [ ] Open **Profile**.
- [ ] Verify "Business Identity" card contains the entered Name and Location.
- [ ] Click "Edit Profile" (Verify micro-interaction/Snackbar).
- [ ] Verify profile stats (Reviews, Verification) display correctly.

### 4. Connectivity & Hardening
- [ ] **App Check Token**: Verify Logcat prints `AppCheckDebugProvider` and token is registered in Console.
- [ ] **Firebase Sync**: Change a value in Firestore (e.g. Venue Name) and verify it reflects in-app on Profile.

---
*Last Updated: 2026-04-21 (Post Phase 3 Hardening).*
