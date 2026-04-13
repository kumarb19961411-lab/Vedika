# Vedika V2 - Phase 2B Data Continuity Contract

This document defines the high-fidelity mock data contract used to synchronize the Partner Registration Flow (Phase 2A) with the Vendor Shell Screens (Phase 2B).

## Shared Data Model: `VendorMockState`

| Field | Source (Registration) | Destination (Shell) | Type | Status |
| :--- | :--- | :--- | :--- | :--- |
| `ownerName` | **Signup: Full Name** | Dashboard Greeting, Profile Header | User Entered | **Live** |
| `businessName` | **Reg: Business/Venue Name** | Dashboard Title, Profile Header | User Entered | **Live** |
| `location` | **Reg: Address/Map** | Dashboard Location, Profile Stats | User Entered | **Live** |
| `capacity` | **Reg: Guest Capacity** (Venue) | Dashboard Highlights | User Entered | **Live** |
| `pricing` | **Reg: Daily Base Price** | Dashboard Highlights, Service Cards | User Entered | **Live** |
| `amenities` | **Reg: Premium Features** | Dashboard Amenities Grid | User Entered | **Live** |
| `yearsExperience` | **Reg: Exp Dropdown** (Decorator) | Decorator Dashboard Hero | User Entered | **Live** |
| `packageTiers` | **Reg: Tier 1/2 Price & Inclusions** | Decorator Service Collections | User Entered | **Live** |
| `coverImage` | Category Selection (Implicit) | Hero Section, Asset Preview | Default Mock | **Placeholder** |
| `rating` | N/A | Dashboard Highlights, Analytics | Random Mock | **Placeholder** |
| `leadsCount` | N/A | Dashboard Stats Bar | Random Mock | **Placeholder** |

## Data Continuity Logic

1. **Personalization**: The `NAMASTE, {ownerName}` greeting is derived directly from the partner's input during the Signup phase.
2. **Context Awareness**: The `DashboardViewModel` identifies the `vendorType` (VENUE vs DECORATOR) from the registration choice to render the appropriate specialized dashboard.
3. **Media Handling**: In the absence of a real image uploader, high-fidelity hero images are assigned based on the primary category selected (e.g., Luxury Hall for Venues, Floral Mandap for Decorators).

## Implementation Traceability

- **State Capture**: `feature:auth:AuthViewModel.saveRegistrationData`
- **Mock Persistence**: `core:data:VendorRepository` (In-memory flow)
- **Consumption**: `feature:dashboard:DashboardViewModel`
- **UI Exposure**: `VenueDashboardScreen`, `DecoratorDashboardScreen`, `ProfileScreen`
