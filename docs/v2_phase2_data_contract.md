# Phase 2B Data Continuity Contract

## Overview
This document defines the data handoff from the registration funnel (`feature:auth`) to the vendor shell (`feature:dashboard`, etc.). This contract ensures that the dashboard feels like a direct continuation of the onboarding experience.

## The Mock Data Lifecycle

1. **Capture (`feature:auth`)**: 
   - User inputs `Full Name`, `Business Name`, `Location`, `Capacity` (Venue), `Service Pricing`, etc.
   - These are persisted in `AuthViewModel` during the funnel.
   
2. **Commit (`VendorRepository`)**:
   - On completion of registration, the data is pushed to `VendorRepository.updateMockVendor()`.
   - This repository acts as a singleton source of truth within the current process lifecycle.

3. **Consume (`Vendor Shell`)**:
   - Tab-level ViewModels (Dashboard, Inventory, Gallery, Profile) collect from `VendorRepository.getMockVendor()`.
   - Screens dynamically adapt their UI based on the `VendorType` found in this state.

## Schema: VendorMockState

| Field | Source (Registration) | Consumer (Dashboard/Shell) |
| :--- | :--- | :--- |
| `ownerName` | Signup Screen (Full Name) | Greeting ("Namaste, Ravi") |
| `businessName` | Registration Form | Header & Profile Branding |
| `location` | Registration Form | Header Location Tag |
| `vendorType` | Category Selection | Screen Selection (Venue vs Dec) |
| `capacity` | Venue Form | Venue Dashboard Stats |
| `basePricing` | Registration Form | Inventory / Package Preview |
| `amenities` | Venue Form | Venue Dashboard (Amenity Cards) |
| `inventoryItems` | Inferred | Inventory Hub |
| `galleryImages` | Inferred / Placeholders | Visual Gallery Portfolio |

## Failover & Enrichment Logic
If a user bypasses registration or fields are missing, the ViewModels apply **High-Fidelity Seed Data** based on the `VendorType`:

- **Venue Default**: "Grand Heritage Estate", 15,000 Sq Ft, 1200 Guests.
- **Decorator Default**: "Couture Design Studio", 12k Impressions, Premium Reach.

## Data Governance
- `AuthRepository`: Handles the "Vendor" model (User identity/ID).
- `VendorRepository`: Handles the "VendorMockState" (Rich UI content).
- **Rule**: No core data field should be hardcoded in more than one place. ViewModels must derive display strings from central state.
