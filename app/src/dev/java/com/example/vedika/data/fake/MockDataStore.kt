package com.example.vedika.data.fake

import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.ZoneId

/**
 * Shared singleton to hold mock data and prevent Hilt circular dependencies
 * between FakeBookingRepository and FakeCalendarRepository.
 */
object MockDataStore {
    val bookings = MutableStateFlow<List<Booking>>(
        listOf(
            Booking("b1", "mock-vendor-id", "Aarav Sharma",  System.currentTimeMillis() + 86400000L * 5,  BookingStatus.CONFIRMED, 150000.0),
            Booking("b2", "mock-vendor-id", "Priya Patel",   System.currentTimeMillis() + 86400000L * 12, BookingStatus.PENDING,   220000.0),
            Booking("b3", "mock-vendor-id", "Rohan Mehta",   System.currentTimeMillis() + 86400000L * 20, BookingStatus.PENDING,   180000.0),
            Booking("b4", "mock-vendor-id", "Sneha Joshi",   System.currentTimeMillis() - 86400000L * 10, BookingStatus.COMPLETED, 300000.0),
            Booking("b5", "mock-vendor-id", "Vikram Nair",   System.currentTimeMillis() - 86400000L * 30, BookingStatus.CONFIRMED, 95000.0),
        )
    )

    val dayBlocks = MutableStateFlow<Map<Pair<String, LocalDate>, String>>(
        mapOf(("mock-vendor-id" to LocalDate.now().plusDays(2)) to "Annual Maintenance")
    )
}
