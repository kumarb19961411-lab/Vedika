# Locked Milestone 2 Plan: Consumer Entry & Discovery

This document serves as the formal, locked specification for Milestone 2. No changes to the scope or terminal path are permitted without a formal re-planning session.

## 📍 Final Scope Identification
- **User Home**: High-fidelity landing with featured carousels.
- **Discovery**: Category-driven browse and vendor profile views.
- **Routing**: `USER` role correctly routed to Home; `PARTNER` unaffected.
- **Session**: Seamless restore for both roles.
- **Terminal Action**: "Send Inquiry" (Lead capture v1).

## 🧊 Frozen Components
### Routes
- `user_home`
- `vendor_browse/{category}`
- `vendor_detail/{id}`
- `inquiry_form/{id}`

### Module Topology
- `feature:discovery` will isolate all consumer-facing logic.
- `app` will handle the dynamic bottom navigation switch.

## 🚀 Execution Sequence (Smallest Safe Slices)
1. **Infrastructure**: Role-aware Nav Shell (Verify Partner flows).
2. **Entry**: Routing logic (Splash/Auth) redirection to `user_home`.
3. **Browse**: Discovery screens (Mock data integration).
4. **Action**: Terminal inquiry flow implementation.

## 🛡️ Android Build Regression Guard
- [ ] Role Isolation: `USER` must never reach `PARTNER` routes.
- [ ] Partner Stability: Calendar and Inventory must remain 100% functional.
- [ ] Session Restore: App cold-start must resolve correct role home.

## 🚩 No-Go Conditions
- DO NOT implement payment processing.
- DO NOT implement real-time availability validation.
- DO NOT touch partner-side inventory persistence logic.
