# Phase 2 Data Contract — Shared Mock Vendor State (Updated)

This document defines the structured mock state shared between the Registration flow and the Vendor Shell (Dashboard/Profile) implemented in Phase 2A.

## 📊 Vendor Data Model

```kotlin
data class VendorMockState(
    val businessName: String,
    val venueName: String? = null,    // Populate from venueName in VenueRegistration
    val location: String,             // User input (Venue) or Default (Decorator)
    val capacity: String? = null,     // User input (Venue)
    val pricing: String,              // User input (Base Price / Tier 1 Price)
    val amenities: List<String>,      // User selected IDs/names
    val coverImage: String,           // Mock Default for Phase 2A
    val galleryImages: List<String>,  // Empty for Phase 2A
    val vendorType: VendorType,       // Derived from Category Selection
    val primaryCategory: String,      // Category name from Selection
    val ownerName: String             // Mock Default for Phase 2A
)

enum class VendorType { VENUE, DECORATOR }
```

## 🔌 Connection Points

### 1. Registration (Producer)
Registration state is hoisted in the graph-scoped `AuthViewModel` and "committed" to the `FakeVendorRepository` only upon successful completion.

- **VenueRegistrationScreen**: Populates `venueName`, `venueCapacity`, `venueLocation`, `venuePrice`, and `venueAmenities`.
- **DecoratorRegistrationScreen**: Populates `decoratorBusinessName`, `decoratorTier1Price`, and `decoratorExpertise`.
- **Primary Logic**: The `AuthViewModel.saveRegistrationData()` method maps these screen-specific fields into the unified `VendorMockState`.

### 2. Dashboard (Consumer)
Future Phase 2B screens should consume data from `VendorRepository.getMockVendor()`.

| Consumer Screen | Main Data Fields Consumed |
| :--- | :--- |
| **VenueDashboard** | `businessName`, `capacity`, `amenities`, `pricing` |
| **DecoratorDashboard** | `businessName`, `pricing`, `amenities` |
| **VendorProfile** | `ownerName`, `coverImage`, `location`, `primaryCategory` |

## 🛠️ Mock Defaults (Phase 2A)

The following fields use documented mock defaults to ensure the model is complete for rendering in future passes:

- **Location (Decorator)**: `"Main Market, Hyderabad"`
- **Owner Name**: `"Heritage Partner Admin"` (Venue) or `"Luxe Decor Admin"` (Decorator)
- **Cover Image**: Generic Unsplash architectural/floral URLs.
- **Gallery**: Currently initialized as an empty list `[]`.

## 💾 Implementation Authority
- **Model**: `com.example.vedika.core.data.model.VendorMockState`
- **Repository**: `com.example.vedika.core.data.repository.VendorRepository`
- **Storage**: `com.example.vedika.data.fake.FakeVendorRepository` (Singleton)
