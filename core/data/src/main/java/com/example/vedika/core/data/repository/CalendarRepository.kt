package com.example.vedika.core.data.repository

import com.example.vedika.core.data.model.CalendarDayState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface CalendarRepository {
    /**
     * Returns the calendar state for a specific month and year.
     * The implementation should handle the logic for merging bookings, manual blocks,
     * and capacity calculations based on the vendor type.
     */
    fun getCalendarState(vendorId: String, month: Int, year: Int): Flow<Map<LocalDate, CalendarDayState>>

    /**
     * Manually blocks a date for a specific reason.
     */
    suspend fun blockDate(vendorId: String, date: LocalDate, reason: String): Result<Unit>

    /**
     * Unblocks a previously manually blocked date.
     */
    suspend fun unblockDate(vendorId: String, date: LocalDate): Result<Unit>

    /**
     * Returns true if the date is manually blocked or blocked by system conflicts.
     */
    suspend fun isDateBlocked(vendorId: String, date: LocalDate): Boolean
}
