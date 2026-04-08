package com.example.vedika.data.fake

import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeBookingRepository @Inject constructor() : BookingRepository {
    private val bookings = MutableStateFlow<List<Booking>>(
        listOf(
            Booking("b1", "dev_vendor_001", "Aarav Sharma",  System.currentTimeMillis() + 86400000L * 5,  BookingStatus.CONFIRMED, 150000.0),
            Booking("b2", "dev_vendor_001", "Priya Patel",   System.currentTimeMillis() + 86400000L * 12, BookingStatus.PENDING,   220000.0),
            Booking("b3", "dev_vendor_001", "Rohan Mehta",   System.currentTimeMillis() + 86400000L * 20, BookingStatus.PENDING,   180000.0),
            Booking("b4", "dev_vendor_001", "Sneha Joshi",   System.currentTimeMillis() - 86400000L * 10, BookingStatus.COMPLETED, 300000.0),
            Booking("b5", "dev_vendor_001", "Vikram Nair",   System.currentTimeMillis() - 86400000L * 30, BookingStatus.CONFIRMED, 95000.0),
        )
    )

    override fun getBookingsForVendor(vendorId: String): Flow<List<Booking>> {
        return bookings.map { list -> list.filter { it.vendorId == vendorId } }
    }

    override suspend fun createBooking(booking: Booking): Result<Unit> {
        val current = bookings.value.toMutableList()
        current.add(0, booking)          // newest first
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
}
