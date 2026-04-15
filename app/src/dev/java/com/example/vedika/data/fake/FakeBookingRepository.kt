package com.example.vedika.data.fake

import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

import com.example.vedika.core.data.model.SlotType
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.VendorRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Singleton
class FakeBookingRepository @Inject constructor(
    private val vendorRepository: VendorRepository
) : BookingRepository {
    private val bookings = MockDataStore.bookings
    private val dayBlocks = MockDataStore.dayBlocks

    override fun getBookingsForVendor(vendorId: String): Flow<List<Booking>> {
        return bookings.map { list -> list.filter { it.vendorId == vendorId } }
    }

    override suspend fun createBooking(booking: Booking): Result<Unit> {
        val targetDate = Instant.ofEpochMilli(booking.eventDate).atZone(ZoneId.systemDefault()).toLocalDate()
        
        // 1. Check for manual blocks
        if (dayBlocks.value.containsKey(booking.vendorId to targetDate)) {
            return Result.failure(Exception("DATE_BLOCKED: This date has been manually blocked by the vendor."))
        }

        val conflict = checkConflict(booking.vendorId, booking.eventDate, booking.slotType).getOrNull() ?: false
        if (conflict) {
            val vendor = vendorRepository.getMockVendor().firstOrNull()
            val error = if (vendor?.vendorType == VendorType.VENUE) "SLOT_OCCUPIED" else "CAPACITY_FULL"
            return Result.failure(Exception("$error: Conflict detected with existing booking."))
        }

        val current = bookings.value.toMutableList()
        current.add(0, booking)
        bookings.value = current
        return Result.success(Unit)
    }

    override suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Result<Unit> {
        val current = bookings.value.toMutableList()
        val index = current.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            current[index] = current[index].copy(status = status)
            bookings.value = current
            return Result.success(Unit)
        }
        return Result.failure(Exception("Booking not found"))
    }

    override suspend fun checkConflict(vendorId: String, date: Long, slotType: SlotType): Result<Boolean> {
        val vendor = vendorRepository.getMockVendor().firstOrNull() 
            ?: return Result.failure(Exception("Vendor not found"))
        
        val targetDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
        val dayBookings = bookings.value.filter { 
            val bDate = Instant.ofEpochMilli(it.eventDate).atZone(ZoneId.systemDefault()).toLocalDate()
            bDate == targetDate && it.status != BookingStatus.CANCELLED
        }

        return if (vendor.vendorType == VendorType.VENUE) {
            val hasConflict = dayBookings.any { b ->
                when (slotType) {
                    SlotType.FULL_DAY -> true // Conflicts with everything
                    SlotType.MORNING -> b.slotType == SlotType.MORNING || b.slotType == SlotType.FULL_DAY
                    SlotType.EVENING -> b.slotType == SlotType.EVENING || b.slotType == SlotType.FULL_DAY
                }
            }
            Result.success(hasConflict)
        } else {
            // Decorator: Max 4 concurrent jobs
            Result.success(dayBookings.size >= 4)
        }
    }
}
