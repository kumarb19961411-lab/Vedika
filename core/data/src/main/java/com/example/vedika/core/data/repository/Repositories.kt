package com.example.vedika.core.data.repository

import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.model.InventoryItem
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getActiveVendor(): Flow<VendorUser?>
    suspend fun loginAsDevBypass(username: String): Result<VendorUser>
    suspend fun logout()
}

interface VendorRepository {
    suspend fun getVendorProfile(vendorId: String): Result<VendorUser>
    suspend fun updateBusinessName(vendorId: String, newName: String): Result<Unit>
}

interface BookingRepository {
    fun getBookingsForVendor(vendorId: String): Flow<List<Booking>>
    suspend fun createBooking(booking: Booking): Result<Unit>
    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Result<Unit>
}

interface InventoryRepository {
    fun getInventoryForVendor(vendorId: String): Flow<List<InventoryItem>>
    suspend fun addInventoryItem(item: InventoryItem): Result<Unit>
    suspend fun updateInventoryAvailability(itemId: String, isAvailable: Boolean): Result<Unit>
}
