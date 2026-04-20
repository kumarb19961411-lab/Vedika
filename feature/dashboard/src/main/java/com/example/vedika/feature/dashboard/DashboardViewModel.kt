package com.example.vedika.feature.dashboard

import android.util.Log
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
    val totalBookings: Int = 0,
    val cancelledCount: Int = 0,
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

    private fun logDebug(message: String) {
        Log.d("VedikaDebug", "DashboardViewModel: $message")
    }

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            logDebug("Resolving User ID...")
            val uid = authRepository.getCurrentUserId()
            if (uid == null) {
                logDebug("No active UID found. Dashboard loading failed.")
                _uiState.value = _uiState.value.copy(isLoading = false)
                return@launch
            }

            logDebug("Subscribing to Vendor Profile: $uid")
            vendorRepository.getVendorProfileStream(uid).collect { profile ->
                if (profile != null) {
                    logDebug("Canonical Profile Loaded: ${profile.businessName} (${profile.vendorType})")
                    _uiState.value = _uiState.value.copy(
                        businessName = profile.businessName,
                        vendorName = profile.ownerName,
                        location = profile.location,
                        pricing = profile.pricing,
                        capacity = profile.capacity,
                        amenities = profile.amenities,
                        coverImage = profile.coverImage ?: "",
                        vendorType = profile.vendorType,
                        yearsExperience = profile.yearsExperience,
                        packageTiers = profile.packageTiers,
                        rating = profile.rating,
                        leadsCount = profile.leadsCount,
                        area = profile.area,
                        venueType = profile.venueType,
                        isLoading = false
                    )
                    
                    // Fetch real bookings for this vendor
                    loadBookings(profile.id)
                } else {
                    logDebug("No Vendor Profile document found for UID: $uid. Attempting fallback to Auth identity.")
                    // This scenario should primarily happen for brand new users before profile persistence completes
                    authRepository.getActiveVendor().collect { vendor ->
                        if (vendor != null) {
                            _uiState.value = _uiState.value.copy(
                                businessName = vendor.businessName,
                                vendorName = vendor.ownerName,
                                vendorType = if (vendor.primaryServiceCategory.contains("Venue", ignoreCase = true)) VendorType.VENUE else VendorType.DECORATOR,
                                isLoading = false
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
        if (vendorId.isBlank() || vendorId == "mock-vendor-id") {
            logDebug("Invalid Vendor ID for bookings. Skipping fetch.")
            return
        }
        
        logDebug("Fetching real-time bookings for Vendor: $vendorId")
        viewModelScope.launch {
            bookingRepository.getBookingsForVendor(vendorId).collect { bookings ->
                _uiState.value = _uiState.value.copy(
                    upcomingBookings = bookings
                        .filter { it.status != BookingStatus.CANCELLED && it.eventDate > System.currentTimeMillis() }
                        .sortedBy { it.eventDate }
                        .take(5),
                    pendingCount = bookings.count { it.status == BookingStatus.PENDING },
                    confirmedCount = bookings.count { it.status == BookingStatus.CONFIRMED },
                    cancelledCount = bookings.count { it.status == BookingStatus.CANCELLED },
                    totalBookings = bookings.size,
                    totalRevenue = bookings.filter { it.status == BookingStatus.CONFIRMED }.sumOf { it.totalAmount },
                    isLoading = false
                )
            }
        }
    }
}
