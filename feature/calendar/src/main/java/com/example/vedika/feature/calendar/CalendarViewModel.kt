package com.example.vedika.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CalendarUiState(
    val allBookings: List<Booking> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            authRepository.getActiveVendor().collect { vendor ->
                if (vendor != null) {
                    bookingRepository.getBookingsForVendor(vendor.id).collect { bookings ->
                        _uiState.value = CalendarUiState(
                            allBookings = bookings.sortedBy { it.eventDate },
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    fun confirmBooking(bookingId: String) {
        viewModelScope.launch {
            bookingRepository.updateBookingStatus(bookingId, BookingStatus.CONFIRMED)
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            bookingRepository.updateBookingStatus(bookingId, BookingStatus.CANCELLED)
        }
    }
}
