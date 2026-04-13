package com.example.vedika.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.BookingRepository
import com.example.vedika.core.data.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val vendorName: String = "",
    val businessName: String = "",
    val venueName: String? = null,
    val location: String = "",
    val capacity: String? = null,
    val pricing: String? = null,
    val area: String? = null,
    val venueType: String? = null,
    val rating: String? = null,
    val leadsCount: String? = null,
    val yearsExperience: String? = null,
    val packageTiers: List<com.example.vedika.core.data.model.PackageTier> = emptyList(),
    val analyticsSummary: Map<String, String>? = null,
    val amenities: List<String> = emptyList(),
    val coverImage: String = "",
    val vendorType: VendorType = VendorType.VENUE,
    val upcomingBookings: List<Booking> = emptyList(),
    val pendingCount: Int = 0,
    val confirmedCount: Int = 0,
    val totalRevenue: Double = 0.0,
    val isLoading: Boolean = true
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository,
    private val vendorRepository: VendorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            // First, try to get the mock vendor state for continuity
            vendorRepository.getMockVendor().collect { mockState ->
                if (mockState != null) {
                    _uiState.value = _uiState.value.copy(
                        businessName = mockState.businessName,
                        venueName = mockState.venueName,
                        location = mockState.location,
                        capacity = mockState.capacity,
                        pricing = mockState.pricing,
                        yearsExperience = mockState.yearsExperience,
                        packageTiers = mockState.packageTiers,
                        area = mockState.area,
                        venueType = mockState.venueType,
                        rating = mockState.rating,
                        leadsCount = mockState.leadsCount,
                        analyticsSummary = mockState.analyticsSummary,
                        amenities = mockState.amenities,
                        coverImage = mockState.coverImage,
                        vendorType = mockState.vendorType,
                        vendorName = mockState.ownerName,
                        isLoading = false
                    )
                    // Fetch bookings for this mock vendor (using a mock ID or owner name)
                    loadBookings("mock-vendor-id")
                } else {
                    // Fallback to active vendor from auth
                    authRepository.getActiveVendor().collect { vendor ->
                        if (vendor != null) {
                            _uiState.value = _uiState.value.copy(
                                businessName = vendor.businessName,
                                vendorName = vendor.ownerName,
                                vendorType = if (vendor.primaryServiceCategory.contains("Venue", ignoreCase = true)) VendorType.VENUE else VendorType.DECORATOR
                            )
                            loadBookings(vendor.id)
                        } else {
                            _uiState.value = _uiState.value.copy(isLoading = false)
                        }
                    }
                }
            }
        }
    }

    private fun loadBookings(vendorId: String) {
        viewModelScope.launch {
            bookingRepository.getBookingsForVendor(vendorId).collect { bookings ->
                _uiState.value = _uiState.value.copy(
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
        }
    }
}
