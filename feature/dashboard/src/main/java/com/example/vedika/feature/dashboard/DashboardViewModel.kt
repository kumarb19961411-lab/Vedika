package com.example.vedika.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val vendorName: String = "",
    val businessName: String = "",
    val upcomingBookings: List<Booking> = emptyList(),
    val pendingCount: Int = 0,
    val confirmedCount: Int = 0,
    val totalRevenue: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            authRepository.getActiveVendor().collect { vendor ->
                if (vendor != null) {
                    bookingRepository.getBookingsForVendor(vendor.id).collect { bookings ->
                        _uiState.value = DashboardUiState(
                            vendorName = vendor.ownerName,
                            businessName = vendor.businessName,
                            upcomingBookings = bookings
                                .filter { it.status != BookingStatus.CANCELLED && it.eventDate > System.currentTimeMillis() }
                                .sortedBy { it.eventDate }
                                .take(5),
                            pendingCount = bookings.count { it.status == BookingStatus.PENDING },
                            confirmedCount = bookings.count { it.status == BookingStatus.CONFIRMED },
                            totalRevenue = bookings.filter { it.status == BookingStatus.CONFIRMED }.sumOf { it.totalAmount },
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
            }
        }
    }
}
