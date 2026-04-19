package com.example.vedika.core.data.repository

import com.example.vedika.core.data.model.*
import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.model.InventoryItem
import com.example.vedika.core.data.model.SlotType
import com.example.vedika.core.data.model.VendorMockState
import com.example.vedika.core.data.model.VendorUser
import kotlinx.coroutines.flow.Flow

import android.app.Activity

interface AuthRepository {
    fun getActiveVendor(): Flow<VendorUser?>
    suspend fun sendOtp(phoneNumber: String, activity: Activity): Result<String>
    suspend fun verifyOtp(verificationId: String, otp: String): Result<VendorUser>
    suspend fun loginAsDevBypass(username: String): Result<VendorUser>
    suspend fun logout()
}

interface UserRepository {
    suspend fun getUserProfile(uid: String): Result<AppUser>
    suspend fun createUserProfile(user: AppUser): Result<Unit>
    fun getUserProfileStream(uid: String): Flow<AppUser?>
}

interface VendorRepository {
    suspend fun getVendorProfile(vendorId: String): Result<VendorUser>
    fun getVendorProfileStream(vendorId: String): Flow<VendorUser?>
    suspend fun updateBusinessName(vendorId: String, newName: String): Result<Unit>
    
    // Mock continuity methods
    fun getMockVendor(): Flow<VendorMockState?>
    suspend fun saveMockVendor(state: VendorMockState): Result<Unit>
}

interface BookingRepository {
    fun getBookingsForVendor(vendorId: String): Flow<List<Booking>>
    suspend fun createBooking(booking: Booking): Result<Unit>
    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Result<Unit>
    suspend fun checkConflict(vendorId: String, date: Long, slotType: SlotType): Result<Boolean>
}

interface InventoryRepository {
    fun getInventoryForVendor(vendorId: String): Flow<List<InventoryItem>>
    suspend fun addInventoryItem(item: InventoryItem): Result<Unit>
    suspend fun updateInventoryAvailability(itemId: String, isAvailable: Boolean): Result<Unit>
}
