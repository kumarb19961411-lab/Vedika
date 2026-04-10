# Emulator Test Checklist — Vedika V2

This checklist ensures the core auth and registration flows are bug-free. Use the `devDebug` build variant for testing.

## 🛠 Pre-Test Setup
- [ ] Build variant set to `devDebug`.
- [ ] Clear App Data if testing a fresh install experience.
- [ ] Ensure `FakeAuthRepository` is the active binding (default in `dev`).

---

## 🏗 Flow 1: Sign Up as User (Register)
1. **Screen: Login**
   - Click "Register your account" footer.
2. **Screen: Sign Up**
   - Toggle to "User Sign Up".
   - Enter a valid 10-digit mobile number.
   - Click "Send OTP".
3. **Screen: OTP Verification**
   - Enter `1234`.
   - Click "Verify".
4. **Expected Result**: Navigates to **Category Selection** screen.

## 🏗 Flow 2: Sign Up as Partner (Register)
1. **Screen: Signup**
   - Toggle to "Partner Sign Up".
   - Enter number -> Click "Send OTP".
2. **Screen: OTP Verification**
   - Enter `1234` -> Click "Verify".
3. **Expected Result**: Navigates to **Category Selection** screen.

## 🏗 Flow 3: Sign In (Existing User)
1. **Screen: Login**
   - Toggle to "User Login" or "Partner Login".
   - Click "Send OTP".
2. **Screen: OTP Verification**
   - Enter `1234`.
3. **Expected Result**: Navigates directly to **Dashboard** (Bypasses Registration).

---

## 🧪 Interactive Interaction Guard
- [ ] **Validation**: Verify "Continue" buttons are disabled until required fields are filled on registration screens.
- [ ] **Feedback**: Ensure the "Draft Saved" Snackbar appears when clicking "Save Draft".
- [ ] **Back-stack**: Ensure clicking "Back" from Category Selection returns you to the OTP screen properly.
- [ ] **Number Pad**: Ensure phone and price fields trigger a numeric keyboard.

## 📱 Visual Sanity Check
- [ ] No layout overlapping on smaller emulator screens (e.g., Pixel 4).
- [ ] Glassmorphism effects are visible and fluid during scrolling.
- [ ] Text contrasting is sufficient on brand-orange backgrounds.
