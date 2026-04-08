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
