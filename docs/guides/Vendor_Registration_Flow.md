---
tags:
  - guide
  - vendor
  - registration
---
# 🏁 Vendor Registration Flow

This guide describes the registration process for vendors in Vedika.

## 🗺️ Journey Overview
1. **Auth Gateway**: User signs in with phone number and enters OTP.
2. **Role Selection**: User chooses "Register as Partner".
3. **Business Profile**: User enters business name, category, and contact details.
4. **Verification**: (Optional/Admin-driven) Business details are reviewed.
5. **Dashboard Entry**: User is routed to the Vendor Dashboard.

## 🛠️ Implementation Details

### Navigation Logic
The registration flow is managed via the `auth_graph`. Once a user selects the vendor role, they are pushed to the registration destination.
> [!IMPORTANT]
> **Backstack Total Annihilation**: Upon completion of registration, the `auth_graph` is cleared from the backstack to prevent the user from navigating back to the registration forms.

### Data Persistence
Vendor details are saved to the `/vendors` collection in Firestore. The document ID matches the Firebase Auth `uid`.

---
[[Vendor_Flow_Hub|🏢 Vendor Flow Hub]] | [[Project_Hub|🏠 Project Hub]]
