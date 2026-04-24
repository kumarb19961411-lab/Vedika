# 🛡️ Firebase Security Rules Remediation Plan

- **Status**: DRAFT / PENDING EXECUTION
- **Target Phase**: Milestone 4 Post-Bash Remediation
- **Gate**: BETA RELEASE

## 📋 Overview
The current Firebase security rules allow broad read access to the discovery module but lack granular protection for sensitive operational and user data. This plan outlines the transition to a **"Public Discovery, Private Operations"** model.

## ⚖️ Core Principles
1. **Public Discovery**: Images and basic vendor profiles must be readable by unauthenticated users to support the discovery engine.
2. **Private Operations**: KYC data, financial records, and specific inventory counts must be restricted to authenticated owners or authorized consumers.
3. **Write Protection**: No collection should allow unauthenticated writes. All writes must be tied to a verified `request.auth.uid`.

---

## 🔒 Proposed Firestore Rules Structure (`firestore.rules`)

### 1. Vendors Collection (`/vendors/{vendorId}`)
- **Read**: Allow public access to non-sensitive fields (name, description, location, rating).
- **Read (Sensitive)**: Restrict access to `revenue_metrics`, `kyc_status`, and `private_contact` to the owner (`resource.data.ownerId == request.auth.uid`).
- **Write**: Only the verified owner can create or update.

### 2. Bookings Collection (`/bookings/{bookingId}`)
- **Read**: Only the `vendorId` (owner) and the `userId` (customer) can read the booking details.
- **Write**: 
  - **Create**: Authenticated users can create a booking.
  - **Update**: Only the vendor or customer can update status.

### 3. Inventory & Occupancy (`/inventory/{id}`, `/occupancy/{id}`)
- **Read**: Public access for availability checking.
- **Write**: Strictly restricted to the vendor owner.

---

## 📂 Proposed Storage Rules Structure (`storage.rules`)

### 1. Vendor Gallery (`/vendors/{vendorId}/gallery/{allPaths=**}`)
- **Read**: Allow public access to enable discovery image carousels.
- **Write**: Restrict to `vendorId` owner.

### 2. User Profiles (`/users/{userId}/avatar.jpg`)
- **Read**: Public (for display in reviews).
- **Write**: Restrict to `userId` owner.

---

## 🛠️ Implementation Steps
1. **Data Migration**: Ensure all vendor documents have an `ownerId` field that matches their Firebase Auth UID.
2. **Rule Deployment**: Deploy the new rules to the Firebase Console.
3. **Integration Testing**: Use the Firebase Emulator Suite to verify that unauthenticated users can still see vendors but cannot access sensitive fields or create bookings without signing in.
4. **App Check Enforcement**: Enable App Check to prevent non-app traffic from bypassing rules via raw REST calls.

## 🧪 Verification Plan
- [ ] **Unauthenticated Test**: Verify `GET /vendors` succeeds but `GET /bookings` fails.
- [ ] **Owner Test**: Verify vendor can update their own inventory.
- [ ] **Impersonation Test**: Verify Vendor A cannot read Vendor B's revenue data.
