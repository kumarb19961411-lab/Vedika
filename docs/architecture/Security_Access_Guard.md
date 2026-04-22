---
tags:
  - architecture
  - security
  - auth
---
# 🛡️ Security Access Guard

The Security Access Guard defines the principles and implementations used to ensure data integrity and privacy across Vedika.

## 🔐 Core Principles
1. **Role-Based Access Control (RBAC)**: All operations are gated by the user's role (CONSUMER, VENDOR, ADMIN).
2. **Global Auth Guard**: A centralized mechanism in the navigation shell that prevents unauthenticated access to protected routes.
3. **Firestore Security Rules**: Server-side enforcement of access policies.

## 🧱 Implementation Details

### Firestore Rules
Rules are structured to allow:
- **Consumers**: Read-only access to vendor catalogs; Read/Write access to their own bookings.
- **Vendors**: Read/Write access to their own inventory and bookings; Read-only access to their public profile.
- **Public**: Limited read access to vendor basic info for discovery.

### Auth Branching
During the startup flow (`SplashViewModel`), the application determines the user's role and redirects them to the appropriate dashboard. If a profile is missing, they are routed to the onboarding flow.

## 🛡️ App Check
Vedika uses **Firebase App Check** to ensure that only legitimate app instances can access the backend. In development, the Debug Provider is used with generated tokens.

## 🔄 Related Docs
- [[role_behavior_matrix|🎭 Role & Behavior Matrix]]
- [[Auth_Hub|🔐 Auth Hub]]
- [[Firebase_Hub|🔥 Firebase Hub]]

---
[[Architecture_Hub|🏗️ Architecture Hub]] | [[Project_Hub|🏠 Project Hub]]
