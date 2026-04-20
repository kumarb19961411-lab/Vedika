---
title: Emulator Verification Checklist
type: notes
status: active
owner: QA Reliability Engineer
phase: Phase 3
last_updated: 2026-04-21
tags: [qa, checklist, emulator, verification]
---

# ✅ Emulator Verification Checklist

Use this checklist to perform a full end-to-end "Smoketest" in the Android Studio emulator (`devDebug` flavor).

## 🏁 1. Initial Launch
- [ ] **Splash → Route**: Launch app, see splash, and arrive at the current auth destination (Login or Dashboard).
- [ ] **App Check Token**: Verify Logcat prints the `DebugAppCheckProvider` secret.

## 🔐 2. Authentication Flow
- [ ] **Signup/Login → OTP**: Enter `+91 99999 99999`, see OTP screen.
- [ ] **OTP Verification**: Enter `123456`, successfully redirect based on role.
- [ ] **Logout/Relaunch**: Logout, close app, relaunch. Verify session is cleared (return to Login).

## 🏪 3. Vendor Journey
- [ ] **New Partner → Category Selection**: Log in with a new number, verify landing on the `CategorySelection` screen.
- [ ] **Partner Registration**: Complete profile (Business Name, Capacity), save, and verify redirect to `Dashboard`.

## 📈 4. Dashboard & Metrics
- [ ] **Dashboard Metrics**: Add a booking via the `BookingHub`. Verify "Total Revenue" and "Total Bookings" increment immediately.
- [ ] **Calendar State**: Open Calendar, verify the new booking appears on the correct date.

## 🚫 5. Operational Rules
- [ ] **Conflict Blocking**: Attempt to add a second booking to the same slot. Verify the app prevents saving or shows a conflict warning.
- [ ] **Inventory Toggle**: Go to Inventory, toggle an item's availability. Return to Booking and verify state persists correctly.

## 🧪 6. Technical Integrity
- [ ] **Offline Resilience**: Disable emulator data, navigate. Verify no crashes and local cache data displays.
- [ ] **UI Responsiveness**: Verify no ANRs or significant frame drops during dashboard scrolling.

---
[[firebase_dev_setup|🛠️ Firebase Dev Setup]] | [[Milestone_1_QA_Checklist|🎯 Milestone 1 QA]] | [[SYSTEM_STATUS|📊 System Status]]
