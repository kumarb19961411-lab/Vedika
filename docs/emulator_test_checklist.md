# Emulator Test Checklist — Vedika V2

This checklist ensures the core auth and registration flows are bug-free. Use the `devDebug` build variant for testing.

## 🛠 Pre-Test Setup
- [ ] Build variant set to `devDebug`.
- [ ] Clear App Data if testing a fresh install experience.
- [ ] Ensure `FakeAuthRepository` is the active binding (default in `dev`).

---

## 🏗 forward Routing: The 4 Paths
### 1. User (Sign Up OR Sign In)
- [ ] Path: (Login OR Signup) -> OTP -> **Dashboard**
- [ ] *Verification*: Verify that Category Selection is COMPLETELY skipped for Users.

### 2. Partner (Sign In)
- [ ] Path: Login -> Partner Login -> OTP -> **Dashboard**
- [ ] *Verification*: Verify it lands on Dashboard (not registration).

### 3. Partner (Sign Up)
- [ ] Path: Signup -> Partner Sign Up -> OTP -> **Category Selection**
- [ ] *Verification*: Verify it enters the registration flow.

---

## 🏗 Backward Routing: Change Number
- [ ] **Sign In Path**: Login -> OTP -> Click "Change Number" -> **Returns to Login Screen**.
- [ ] **Sign Up Path**: Signup -> OTP -> Click "Change Number" -> **Returns to Signup Screen**.
- [ ] *Verification*: Check that the previously entered phone number remains pre-filled.

---

## 🧪 Interaction Guard
- [ ] **Verification Logic**: Verify OTP `1234` triggers success; any other code triggers error text.
- [ ] **Button State**: Confirm "Verify & Proceed" is disabled until exactly 4 digits are entered.
- [ ] **Resend**: Click "Resend OTP" after 30s -> Verify countdown resets and success feedback appears.
- [ ] **Visuals**: Confirm OTP slots highlight correctly on focus.

## 📱 Visual Sanity Check
- [ ] No layout overlapping on smaller emulator screens (e.g., Pixel 4).
- [ ] Glassmorphism effects are visible and fluid during scrolling.
- [ ] Text contrasting is sufficient on brand-orange backgrounds.
