# Vendor Shell Navigation — Vedika V2

This document defines the navigation structure, route mapping, and visibility constraints for the high-fidelity Vendor Shell implemented in Phase 2B.

## 🧭 Navigation Tabs (Bottom Navigation)

The Vendor Shell features a persistent 5-tab navigation bar available only to authenticated Partners (Venues, Decorators, etc.).

| Tab | Destination | Route | Description |
| :--- | :--- | :--- | :--- |
| **Dashboard** | `DashboardScreen` | `dashboard` | Role-aware landing page (Venue vs Decorator). |
| **Calendar** | `CalendarScreen` | `calendar` | Booking grid and schedule management. |
| **Gallery** | `DecoratorsGallery` | `gallery/decorators`| Portfolio and asset showcase. |
| **Inventory** | `InventoryHub` | `inventory` | Core service and item management. |
| **Profile** | `ProfileScreen` | `profile` | Bento-style business management hub. |

## 🛠️ Specialized Dashboard Routing

The `dashboard` route acts as a router that resolves to role-specific implementations based on the `VendorMockState`:

1. **Venue Partners** ➔ Renders **`VenueDashboardScreen`**
   - Focus: Guest capacity, premium amenities, daily pricing.
2. **Decorator Partners** ➔ Renders **`DecoratorDashboardScreen`**
   - Focus: Service collections, package tiers, years of experience.

## 🖇️ Sub-Routes & Drill-Downs

The shell supports non-tabbed sub-destinations for detailed focus work:

- **`inventory_hub`**: Editorial showcase accessible from the Dashboard via "View Highlights".
- **`new_booking`**: Detail creation flow within the Calendar module.

## 🛡️ Visibility Rules (The Guard)

To ensure a seamless onboarding experience, the `BottomNavigationBar` visibility is strictly controlled in `MainActivity.kt`:

- **VISIBLE**: Only if the current route is one of the 5 main shell tabs or their designated sub-routes.
- **HIDDEN**: During Login, Signup, OTP Verification, Category Selection, and Registration forms.

## 🚀 Navigation Implementation
- **Owner**: `:app` module (`MainActivity.kt`, `VedikaDestination.kt`).
- **Logic**: Uses `popUpTo(AuthGraph) { inclusive = true }` upon registration completion to clear the onboarding history and entry into the shell.
