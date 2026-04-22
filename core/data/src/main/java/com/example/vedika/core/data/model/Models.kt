package com.example.vedika.core.data.model

data class AppUser(
    val uid: String,
    val fullName: String,
    val phoneNumber: String,
    val createdAt: Long = System.currentTimeMillis(),
    val userType: String = "USER",
    val profileImageUrl: String? = null
)

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

enum class SlotType {
    MORNING, EVENING, FULL_DAY
}

enum class DayAvailabilityStatus {
    AVAILABLE, PENDING, LIMITED, BOOKED, BLOCKED, UNAVAILABLE
}

data class VenueOccupancy(
    val morningBooked: Boolean = false,
    val eveningBooked: Boolean = false,
    val isFullDay: Boolean = false
)

data class DecoratorCapacity(
    val totalCrew: Int,
    val busyCrew: Int
)

data class CalendarDayState(
    val date: java.time.LocalDate,
    val status: DayAvailabilityStatus,
    val bookings: List<Booking> = emptyList(),
    val manualBlocks: List<String> = emptyList(),
    val venueOccupancy: VenueOccupancy? = null,
    val decoratorCapacity: DecoratorCapacity? = null,
    val inventoryConflicts: List<String> = emptyList()
)

data class Booking(
    val id: String,
    val vendorId: String,
    val customerName: String,
    val eventDate: Long,
    val status: BookingStatus,
    val totalAmount: Double,
    val slotType: SlotType = SlotType.FULL_DAY
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

data class VendorProfile(
    val id: String,
    val businessName: String,
    val ownerName: String,
    val location: String,
    val pricing: String,
    val vendorType: VendorType,
    val primaryCategory: String,
    val isVerified: Boolean = false,
    val capacity: String? = null,
    val amenities: List<String> = emptyList(),
    val coverImage: String? = null,
    val galleryImages: List<String> = emptyList(),
    
    // Vendor Specific Extensions
    val yearsExperience: String? = null,
    val packageTiers: List<PackageTier> = emptyList(),
    
    // Read-only / Dashboard Stats (Optional in registration)
    val rating: String? = null,
    val leadsCount: String? = null,
    val area: String? = null,
    val venueType: String? = null,
    val featuredAssetTitle: String? = null,
    val featuredAssetImage: String? = null,
    val featuredAssetPrice: String? = null,
    val description: String? = null
)
