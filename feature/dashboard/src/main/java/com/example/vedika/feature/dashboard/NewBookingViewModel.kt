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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class NewBookingFormState(
    val customerName: String = "",
    val eventDateMillis: Long = System.currentTimeMillis() + 86400000L * 7, // default: 1 week out
    val totalAmount: String = "",
    val customerNameError: String? = null,
    val amountError: String? = null,
    val isSubmitting: Boolean = false,
    val isSubmitSuccess: Boolean = false,
    val submitError: String? = null
)

@HiltViewModel
class NewBookingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(NewBookingFormState())
    val formState: StateFlow<NewBookingFormState> = _formState

    fun onCustomerNameChange(value: String) {
        _formState.value = _formState.value.copy(
            customerName = value,
            customerNameError = null
        )
    }

    fun onEventDateChange(millis: Long) {
        _formState.value = _formState.value.copy(eventDateMillis = millis)
    }

    fun onTotalAmountChange(value: String) {
        _formState.value = _formState.value.copy(
            totalAmount = value,
            amountError = null
        )
    }

    fun submitBooking(onSuccess: () -> Unit) {
        val state = _formState.value

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
                totalAmount = amount!!
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
                    _formState.value = _formState.value.copy(
                        isSubmitting = false,
                        submitError = e.message ?: "Failed to create booking"
                    )
                }
        }
    }
}
