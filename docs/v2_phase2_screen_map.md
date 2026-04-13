# Phase 2 Screen Map — Scope Hook & Navigation Rules

This document maps target screens to their implementation routes and design sources for Phase 2.

| Screen ID | Target Route | Vendor Relevance | Scope | decision | Bottom Nav |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `category_selection` | `registration/category` | Multi-role | Phase 2A | MODIFY | Hidden |
| `venue_registration` | `registration/venue` | Venue | Phase 2A | MODIFY | Hidden |
| `decorator_registration`| `registration/decorator`| Decorator | Phase 2A | MODIFY | Hidden |
| `venue_dashboard` | `dashboard/venue` | Venue | Phase 2B | CREATE | Visible |
| `vendor_calendar` | `calendar` | Multi-role | Phase 2B | CREATE | Visible |
| `vendor_inventory` | `inventory` | Multi-role | Phase 2B | CREATE | Visible |
| `inventory_hub` | `inventory/hub` | Multi-role | Phase 2B | CREATE | Visible |
| `decorators_gallery` | `gallery/decorators` | Decorator | Phase 2B | CREATE | Visible |
| `vendor_profile` | `profile/vendor` | Multi-role | Phase 2B | CREATE | Visible |

## 🛡️ Do Not Touch In Phase 2A
The following areas are strictly out of scope for the early Phase 2A "Lock" pass:
- Legacy dashboard logic (outside navigation shell integration).
- Real data persistence (Firebase/Firestore) until Phase 2B context is set.
- Payment gateway integration.
- Analytics/Logging sinks.

## 🚀 Phase 2B Hook placeholders
The following components are identified as "Hooks" for Phase 2B. They must be prepared in Phase 2A but not fully implemented:
1. **Shell `NavHost`**: Must support the new dashboard routes.
2. **`VendorViewModel`**: Must bridge the data gap between registration success and dashboard init.
3. **`AssetDirectory`**: Must anticipate `Phase2.zip` structure in the manifest.
