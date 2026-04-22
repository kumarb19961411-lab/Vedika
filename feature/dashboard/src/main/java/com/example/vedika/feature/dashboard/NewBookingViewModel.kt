package com.example.vedika.feature.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.SlotType
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.BookingRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.repository.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle
import java.util.UUID
import javax.inject.Inject

data class NewBookingFormState(
    val customerName: String = "",
    val eventDateMillis: Long = System.currentTimeMillis() + 86400000L * 7,
    val slotType: SlotType = SlotType.FULL_DAY,
    val vendorType: VendorType = VendorType.VENUE,
    val totalAmount: String = "",
    val customerNameError: String? = null,
    val amountError: String? = null,
    val isSlotConflict: Boolean = false,
    val isSubmitting: Boolean = false,
    val isSubmitSuccess: Boolean = false,
    val submitError: String? = null
)

@HiltViewModel
class NewBookingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository,
    private val vendorRepository: VendorRepository,
    private val calendarRepository: CalendarRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _formState = MutableStateFlow(
        NewBookingFormState(
            eventDateMillis = savedStateHandle.get<Long>("selectedDate") ?: (System.currentTimeMillis() + 86400000L * 7)
        )
    )
    val formState: StateFlow<NewBookingFormState> = _formState

    init {
        viewModelScope.launch {
            authRepository.getActiveVendor().collect { vendor ->
                if (vendor != null) {
                    val type = if (vendor.primaryServiceCategory.contains("Venue", ignoreCase = true)) 
                        VendorType.VENUE else VendorType.DECORATOR
                    _formState.update { it.copy(vendorType = type) }
                }
            }
        }
        // Run initial conflict check if prefilled
        checkConflicts()
    }

    fun onCustomerNameChange(value: String) {
        _formState.update { 
            it.copy(customerName = value, customerNameError = null) 
        }
    }

    fun onEventDateChange(millis: Long) {
        _formState.update { it.copy(eventDateMillis = millis) }
        checkConflicts()
    }

    fun onSlotChange(slot: SlotType) {
        _formState.update { it.copy(slotType = slot) }
        checkConflicts()
    }

    private fun checkConflicts() {
        viewModelScope.launch {
            val vendor = authRepository.getActiveVendor().first() ?: return@launch
            
            val bookingConflictResult = bookingRepository.checkConflict(
                vendorId = vendor.id,
                date = _formState.value.eventDateMillis,
                slotType = _formState.value.slotType
            )
            val bookingConflict = bookingConflictResult.getOrDefault(false)

            val targetDate = java.time.Instant.ofEpochMilli(_formState.value.eventDateMillis)
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
            val manualBlock = calendarRepository.isDateBlocked(vendor.id, targetDate)

            _formState.update { it.copy(isSlotConflict = bookingConflict || manualBlock) }
        }
    }

    fun onTotalAmountChange(value: String) {
        _formState.update { 
            it.copy(totalAmount = value, amountError = null) 
        }
    }

    fun submitBooking(onSuccess: () -> Unit) {
        val state = _formState.value
        if (state.isSubmitting) return

        // Validate
        var hasError = false
        if (state.customerName.isBlank()) {
            _formState.value = _formState.value.copy(customerNameError = "Customer name is required")
            hasError = true
        }
        val amount = state.totalAmount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _formState.value = _formState.value.copy(amountError = "Enter a valid amount")
            hasError = true
        }
        if (hasError) return

        _formState.value = _formState.value.copy(isSubmitting = true, submitError = null)

        viewModelScope.launch {
            val vendor = authRepository.getActiveVendor().first()
            if (vendor == null) {
                _formState.value = _formState.value.copy(
                    isSubmitting = false,
                    submitError = "No active vendor session"
                )
                return@launch
            }

            val booking = Booking(
                id = UUID.randomUUID().toString(),
                vendorId = vendor.id,
                customerName = state.customerName.trim(),
                eventDate = state.eventDateMillis,
                status = BookingStatus.PENDING,
                totalAmount = amount!!,
                slotType = state.slotType
            )

            bookingRepository.createBooking(booking)
                .onSuccess {
                    _formState.value = _formState.value.copy(
                        isSubmitting = false,
                        isSubmitSuccess = true
                    )
                    onSuccess()
                }
                .onFailure { e ->
                    val userMessage = when {
                        e.message?.contains("CONFLICT_SLOT_OCCUPIED") == true -> "This slot overlaps with an existing booking."
                        e.message?.contains("CONFLICT_DATE_BLOCKED") == true -> "This date has been manually blocked and is unavailable."
                        e.message?.contains("CONFLICT_CAPACITY_FULL") == true -> "Booking capacity reached for this date."
                        e.message?.contains("ERROR_VENDOR_NOT_FOUND") == true -> "Vendor profile not found. Please re-login."
                        else -> e.message ?: "Failed to create booking. Please try again."
                    }
                    _formState.value = _formState.value.copy(
                        isSubmitting = false,
                        submitError = userMessage
                    )
                }
        }
    }
}
