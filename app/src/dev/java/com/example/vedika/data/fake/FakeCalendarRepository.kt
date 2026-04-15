package com.example.vedika.data.fake

import com.example.vedika.core.data.model.*
import com.example.vedika.core.data.repository.BookingRepository
import com.example.vedika.core.data.repository.CalendarRepository
import com.example.vedika.core.data.repository.VendorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCalendarRepository @Inject constructor(
    private val vendorRepository: VendorRepository,
    private val bookingRepository: BookingRepository
) : CalendarRepository {

    private val manualBlocks = MockDataStore.dayBlocks
    private val bookingsFlow = MockDataStore.bookings

    override fun getCalendarState(vendorId: String, month: Int, year: Int): Flow<Map<LocalDate, CalendarDayState>> {
        return combine(
            vendorRepository.getMockVendor(),
            manualBlocks,
            bookingRepository.getBookingsForVendor(vendorId)
        ) { mockVendor, blocks, bookings ->
            val vendorType = mockVendor?.vendorType ?: VendorType.VENUE
            generateReactiveState(vendorId, vendorType, month, year, blocks, bookings)
        }
    }

    private fun generateReactiveState(
        vendorId: String,
        vendorType: VendorType,
        month: Int,
        year: Int,
        blocks: Map<Pair<String, LocalDate>, String>,
        bookings: List<Booking>
    ): Map<LocalDate, CalendarDayState> {
        val result = mutableMapOf<LocalDate, CalendarDayState>()
        val startDate = LocalDate.of(year, month, 1)
        val daysInMonth = startDate.lengthOfMonth()

        // Seed for base deterministic "natural" state (can be overridden by real bookings/blocks)
        val seed = vendorId.hashCode().toLong() + year * 100 + month
        val random = Random(seed)

        for (day in 1..daysInMonth) {
            val date = LocalDate.of(year, month, day)
            val blockReason = blocks[vendorId to date]
            
            // Filter bookings for this specific date
            val dayBookings = bookings.filter { 
                val bookingDate = java.time.Instant.ofEpochMilli(it.eventDate)
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate()
                bookingDate == date && it.status != BookingStatus.CANCELLED
            }

            // Precedence Rule 1: Confirmed bookings cannot be overridden by blocks
            val hasConfirmed = dayBookings.any { it.status == BookingStatus.CONFIRMED }
            
            val status: DayAvailabilityStatus
            val manualBlocksList = mutableListOf<String>()
            if (blockReason != null && !hasConfirmed) {
                status = DayAvailabilityStatus.BLOCKED
                manualBlocksList.add(blockReason)
            } else {
                // Determine status based on bookings and base random state
                status = computeStatus(date, vendorType, dayBookings, random)
            }

            result[date] = CalendarDayState(
                date = date,
                status = status,
                bookings = dayBookings,
                manualBlocks = manualBlocksList,
                venueOccupancy = if (vendorType == VendorType.VENUE) computeVenueOccupancy(dayBookings) else null,
                decoratorCapacity = if (vendorType == VendorType.DECORATOR) computeDecoratorCapacity(dayBookings) else null
            )
        }
        return result
    }

    private fun computeStatus(date: LocalDate, vendorType: VendorType, dayBookings: List<Booking>, random: Random): DayAvailabilityStatus {
        if (vendorType == VendorType.VENUE) {
            val hasFullDay = dayBookings.any { it.slotType == SlotType.FULL_DAY && it.status == BookingStatus.CONFIRMED }
            val morning = dayBookings.any { it.slotType == SlotType.MORNING && it.status == BookingStatus.CONFIRMED }
            val evening = dayBookings.any { it.slotType == SlotType.EVENING && it.status == BookingStatus.CONFIRMED }
            
            return when {
                hasFullDay || (morning && evening) -> DayAvailabilityStatus.BOOKED
                morning || evening || dayBookings.isNotEmpty() -> DayAvailabilityStatus.PENDING
                else -> {
                    // 5% chance of base "Natural" blocked status for seed realism
                    if (random.nextInt(100) < 5) DayAvailabilityStatus.BLOCKED else DayAvailabilityStatus.AVAILABLE
                }
            }
        } else {
            val totalCrew = 4 // Configurable mock limit
            val confirmedCount = dayBookings.count { it.status == BookingStatus.CONFIRMED }
            val pendingCount = dayBookings.count { it.status == BookingStatus.PENDING }
            val totalActive = confirmedCount + pendingCount

            return when {
                totalActive >= totalCrew -> DayAvailabilityStatus.BOOKED
                totalActive >= totalCrew - 1 -> DayAvailabilityStatus.LIMITED
                else -> DayAvailabilityStatus.AVAILABLE
            }
        }
    }

    private fun computeVenueOccupancy(bookings: List<Booking>): VenueOccupancy {
        val morning = bookings.any { (it.slotType == SlotType.MORNING || it.slotType == SlotType.FULL_DAY) && it.status == BookingStatus.CONFIRMED }
        val evening = bookings.any { (it.slotType == SlotType.EVENING || it.slotType == SlotType.FULL_DAY) && it.status == BookingStatus.CONFIRMED }
        return VenueOccupancy(
            morningBooked = morning,
            eveningBooked = evening,
            isFullDay = bookings.any { it.slotType == SlotType.FULL_DAY && it.status == BookingStatus.CONFIRMED }
        )
    }

    private fun computeDecoratorCapacity(bookings: List<Booking>): DecoratorCapacity {
        return DecoratorCapacity(
            totalCrew = 4,
            busyCrew = bookings.count { it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.PENDING }
        )
    }

    override suspend fun blockDate(vendorId: String, date: LocalDate, reason: String): Result<Unit> {
        // 1. Check if ANY bookings exist for this date
        val dateMillis = date.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
        val hasBookings = bookingsFlow.value.any { b ->
            val bDate = java.time.Instant.ofEpochMilli(b.eventDate)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
            bDate == date && b.status != BookingStatus.CANCELLED
        }

        if (hasBookings) {
            return Result.failure(Exception("ACTIVE_BOOKINGS_EXIST: Cannot block a date that already has active bookings."))
        }

        val current = manualBlocks.value.toMutableMap()
        current[vendorId to date] = reason
        manualBlocks.value = current
        return Result.success(Unit)
    }

    override suspend fun unblockDate(vendorId: String, date: LocalDate): Result<Unit> {
        val current = manualBlocks.value.toMutableMap()
        current.remove(vendorId to date)
        manualBlocks.value = current
        return Result.success(Unit)
    }

    override suspend fun isDateBlocked(vendorId: String, date: LocalDate): Boolean {
        return manualBlocks.value.containsKey(vendorId to date)
    }
}
