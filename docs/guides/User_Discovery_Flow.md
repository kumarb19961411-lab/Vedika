---
tags:
  - guide
  - discovery
  - ui-ux
---
# 👤 User Discovery Flow

This guide outlines the technical implementation and user journey for the Discovery module in Vedika.

## 🗺️ Journey Overview
1. **Home Screen**: User lands on the categories page (`UserHomeScreen`).
2. **Category Selection**: User selects a category (e.g., "Venues", "Photographers").
3. **Vendor Browse**: User views a list of vendors in that category (`VendorBrowseScreen`).
4. **Vendor Detail**: User selects a vendor to view full details (`VendorDetailScreen`).
5. **Inquiry/Booking**: User initiates an inquiry or booking from the detail page.

## 🛠️ Technical Architecture

### ViewModel Hierarchy
The discovery flow is powered by reactive ViewModels that manage state transitions:
- `UserHomeViewModel`: Fetches categories and featured vendors.
- `VendorBrowseViewModel`: Filters and searches vendors by category.
- `VendorDetailViewModel`: Loads comprehensive vendor data, including catalog items and reviews.

### Data Layer
- **Repository**: `VendorRepository` (implemented by `FakeVendorRepository` in dev).
- **Mock Data**: Uses a tiered system where vendor IDs are used to generate consistent mock details.

### Deep Linking
The discovery flow supports deep links to specific vendors:
`vedika://vendor/{vendorId}` -> Routes directly to `VendorDetailScreen`.

## 🧪 Verification
The discovery flow is covered by the `DiscoveryRegressionTest.kt` UI test suite, which simulates a browse journey from category to detail view.

---
[[User_Flow_Hub|👤 User Flow Hub]] | [[Project_Hub|🏠 Project Hub]]
