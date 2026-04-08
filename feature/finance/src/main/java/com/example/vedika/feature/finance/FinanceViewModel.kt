package com.example.vedika.feature.finance

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

data class FinanceUiState(
    val confirmedRevenue: Double = 0.0,
    val pendingRevenue: Double = 0.0,
    val completedRevenue: Double = 0.0,
    val recentTransactions: List<Booking> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class FinanceViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FinanceUiState())
    val uiState: StateFlow<FinanceUiState> = _uiState

    init {
        loadFinance()
    }

    private fun loadFinance() {
        viewModelScope.launch {
            authRepository.getActiveVendor().collect { vendor ->
                if (vendor != null) {
                    bookingRepository.getBookingsForVendor(vendor.id).collect { bookings ->
                        _uiState.value = FinanceUiState(
                            confirmedRevenue = bookings.filter { it.status == BookingStatus.CONFIRMED }.sumOf { it.totalAmount },
                            pendingRevenue = bookings.filter { it.status == BookingStatus.PENDING }.sumOf { it.totalAmount },
                            completedRevenue = bookings.filter { it.status == BookingStatus.COMPLETED }.sumOf { it.totalAmount },
                            recentTransactions = bookings.sortedByDescending { it.eventDate }.take(10),
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
