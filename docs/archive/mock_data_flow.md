# Mock Data Flow & Continuity — Vedika V2

This document describes how user data captured during onboarding flows through the system to provide a personalized and consistent experience in the Vendor Shell.

## 🔄 The Data Pipeline

The pipeline ensures that a partner's registration feels real and persistent, even in a mock-data environment.

1. **Capture**: `AuthViewModel` captures fields from `SignupScreen`, `VenueRegistration`, and `DecoratorRegistration`.
2. **Mapping**: Upon "Complete Registration", `AuthViewModel.saveRegistrationData()` maps UI state to the `VendorMockState` model.
3. **Storage**: The state is stored in the memory-cached `FakeVendorRepository` (Hilt-injected singleton).
4. **Consumption**: `DashboardViewModel` and `ProfileViewModel` query the repository to drive UI binding.

## 📊 Data Mapping Contract

| Implementation Key | Source UI Field | Consumed By |
| :--- | :--- | :--- |
| `ownerName` | Signup: "Full Name" | Dashboard Greeting, Profile Header |
| `businessName` | Registration: Business Name | Dashboard Title, Profile Header |
| `location` | Registration: Address | Dashboard Subtitle, Profile details |
| `yearsExperience` | Registration: Experience selector | Decorator Dashboard Hero |
| `packageTiers` | Registration: Pricing/Inclusions | Decorator Service Collections |

## 🧬 Real vs. Derived vs. Placeholder

To maintain high visual fidelity without a backend, we use three tiers of data:

- **Real (User Entered)**: Name, Business Name, Location, Capacity, Pricing.
- **Derived (Logic)**: 
    - `coverImage`: Assigned based on the category (e.g., Luxury Hall for Venues).
    - `vendorType`: Assigned based on the registration form completed.
- **Placeholder (Static)**: Ratings, Leads count, Analytics graphs (Static Unsplash URLs and randomized numbers).

## 🛠️ Testing Continuity

To verify the flow manually:
1. Clear App Cache or Restart Emulator.
2. Sign Up as a Partner.
3. Enter unique strings for "Full Name" and "Business Name".
4. Complete registration ➔ Verify those exact strings appear on the Dashboard.
