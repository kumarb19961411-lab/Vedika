# Milestone 2: Regression Guard & Acceptance Checklist

## 🛡️ Regression Guard (Partner-Side Stability)
- [ ] **Partner Bottom Nav**: Items [Dashboard, Calendar, Gallery, Inventory, Profile] visible for `PARTNER` role.
- [ ] **Partner Dashboard**: Widgets (Pending Bookings, Quick Stats) load correctly.
- [ ] **In-App Navigation**: `PARTNER` can still access `NewBooking` from Dashboard.
- [ ] **Debug Bypass**: Radio button selector in Login still maps `PARTNER` correctly.
- [ ] **Build Integrity**: `./gradlew :app:assembleDebug` completes without `JdkImageInput` errors.

## ✅ Acceptance Criteria (Consumer Discovery)
- [ ] **Role Isolation**: `USER` account mode **cannot** navigate to `calendar` or `inventory_hub` (routes are guarded).
- [ ] **Dynamic Bottom Bar**: `USER` bottom nav shows [Home, Discovery, Profile].
- [ ] **User Home**: Featured carousels populate with high-fidelity mock/real vendor cards.
- [ ] **Discovery Flow**:
    - [ ] Category chips (Venues, Decorators) handle filtering correctly.
    - [ ] Click Vendor -> Opens `VendorDetail` with gallery.
- [ ] **Action Path**:
    - [ ] From `Detail`, click "Send Inquiry".
    - [ ] Inquiry Form triggers (Mock submission).
    - [ ] Success state shown to user.
- [ ] **Session Restore**:
    - [ ] Kill app as `USER` -> Re-open -> Lands on `UserHome`.
    - [ ] Log out -> Back to Login.

## 🚧 No-Go List
- [X] NO payment gateway integration.
- [X] NO real-time availability checking (Mocked).
- [X] NO vendor-side inquiry management (Milestone 3).
- [X] NO modification of existing Firebase Storage bucket rules.
