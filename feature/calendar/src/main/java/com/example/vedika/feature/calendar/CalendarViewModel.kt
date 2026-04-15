package com.example.vedika.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.model.CalendarDayState
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.BookingRepository
import com.example.vedika.core.data.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class CalendarUiState(
    val selectedMonth: Int = LocalDate.now().monthValue,
    val selectedYear: Int = LocalDate.now().year,
    val selectedDate: LocalDate? = null,
    val monthCalendar: Map<LocalDate, CalendarDayState> = emptyMap(),
    val allBookings: List<Booking> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository,
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    init {
        loadCalendarData()
    }

    private fun loadCalendarData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            authRepository.getActiveVendor().collectLatest { vendor ->
                if (vendor == null) {
                    _uiState.update { it.copy(isLoading = false) }
                    return@collectLatest
                }

                // Combine bookings and calendar matrix
                combine(
                    bookingRepository.getBookingsForVendor(vendor.id),
                    calendarRepository.getCalendarState(
                        vendorId = vendor.id,
                        month = _uiState.value.selectedMonth,
                        year = _uiState.value.selectedYear
                    )
                ) { bookings, matrix ->
                    _uiState.update {
                        it.copy(
                            allBookings = bookings.sortedBy { b -> b.eventDate },
                            monthCalendar = matrix,
                            isLoading = false
                        )
                    }
                }.catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }.collect()
            }
        }
    }

    fun onMonthChange(month: Int, year: Int) {
        _uiState.update { it.copy(selectedMonth = month, selectedYear = year, selectedDate = null) }
        loadCalendarData()
    }

    fun onDateClick(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun confirmBooking(bookingId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            bookingRepository.updateBookingStatus(bookingId, BookingStatus.CONFIRMED)
                .onSuccess { 
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = "Failed to confirm booking: ${e.message}") }
                }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            bookingRepository.updateBookingStatus(bookingId, BookingStatus.CANCELLED)
                .onSuccess { 
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = "Failed to cancel: ${e.message}") }
                }
        }
    }

    fun blockDate(date: LocalDate, reason: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val vendor = authRepository.getActiveVendor().firstOrNull() ?: return@launch
            calendarRepository.blockDate(vendor.id, date, reason)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .onFailure { e ->
                    val userMessage = when {
                        e.message?.contains("ACTIVE_BOOKINGS_EXIST") == true -> 
                            "Cannot block this date because it already has active bookings."
                        else -> "Failed to block date: ${e.message}"
                    }
                    _uiState.update { it.copy(isLoading = false, error = userMessage) }
                }
        }
    }

    fun unblockDate(date: LocalDate) {
        viewModelScope.launch {
            val vendor = authRepository.getActiveVendor().firstOrNull() ?: return@launch
            calendarRepository.unblockDate(vendor.id, date)
        }
    }
}
