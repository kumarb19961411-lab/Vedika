package com.example.vedika.core.data.model

data class VendorUser(
    val id: String,
    val businessName: String,
    val ownerName: String,
    val isVerified: Boolean,
    val primaryServiceCategory: String
)

enum class BookingStatus {
    PENDING, CONFIRMED, CANCELLED, COMPLETED
}

data class Booking(
    val id: String,
    val vendorId: String,
    val customerName: String,
    val eventDate: Long,
    val status: BookingStatus,
    val totalAmount: Double
)

data class InventoryItem(
    val id: String,
    val vendorId: String,
    val name: String,
    val description: String,
    val price: Double,
    val isAvailable: Boolean
)

enum class VendorType { VENUE, DECORATOR }

data class PackageTier(
    val name: String,
    val price: String,
    val inclusions: String
)

data class VendorMockState(
    val businessName: String,
    val venueName: String? = null,    // Only for Venue type
    val location: String,
    val capacity: String? = null,     // Only for Venue type
    val pricing: String,              // Base price or starting from
    val amenities: List<String>,      // List of feature IDs/names
    val coverImage: String,           // URI or asset reference
    val galleryImages: List<String>,  // List of URIs/asset references
    val vendorType: VendorType,       // VENUE or DECORATOR
    val primaryCategory: String,      // Category name from Selection
    val ownerName: String,            // Contact person
    
    // Phase 2B Run C: High-Fidelity Extensions
    val yearsExperience: String? = null,
    val packageTiers: List<PackageTier> = emptyList(),
    
    // Stats & Feedback (Used by Dashboard)
    val featuredAssetTitle: String? = null,
    val featuredAssetImage: String? = null,
    val featuredAssetPrice: String? = null,
    val analyticsSummary: Map<String, String>? = null,
    val area: String? = null,         // e.g. "15,000 Sq Ft"
    val venueType: String? = null,     // e.g. "Indoor/Outdoor"
    val rating: String? = null,       // e.g. "4.8 (212)"
    val leadsCount: String? = null    // e.g. "12 New"
)
