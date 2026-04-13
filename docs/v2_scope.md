# Project Scope & Roadmap — Vedika V2

Vedika V2 focuses on providing a premium, role-aware onboarding journey and a high-fidelity vendor shell experienced through modern Material 3 and Bento-style design.

## 🏁 Phase 2 Status: COMPLETE ✅

### Phase 2A: Registration Shell
- **Isolation**: Bottom bar hidden during onboarding.
- **Fidelity**: Pixel-perfect entry forms for Venues and Decorators.
- **Backstack**: Correct history clearing upon completion.

### Phase 2B: Vendor Shell & Data Continuity
- **Dashboards**: Specialized Bento-style dashboards for Venue and Decorator roles.
- **Profiles**: Modern grid-based profile management.
- **Continuity**: End-to-end data flow from Signup to Dashboard.
- **Global Shell**: Persistence of the 5-tab navigation (Dashboard, Calendar, Gallery, Inventory, Profile).

---

## 🚀 Phase 3: Cloud Integration & Real-time Operations (Future)

Phase 3 will transition the project from high-fidelity mocks to a live production-ready system.

### 1. Firebase Live-Sync
- Replace `VendorRepository` in-memory mocks with real-time Firestore listeners.
- Implement **Firebase Auth** production phone verification (reCAPTCHA/SMS).

### 2. Media Operations
- Implement real **Image Uploads** for portolios and profile banners.
- Support video asset previews in the Inventory Hub.

### 3. Business Logic
- **Booking Engine**: Real-time conflict detection and status transitions (Pending ➔ Confirmed).
- **Leads Integration**: Automated lead generation and inquiry tracking.

---
*Note: This scope document is updated per phase milestone.*
