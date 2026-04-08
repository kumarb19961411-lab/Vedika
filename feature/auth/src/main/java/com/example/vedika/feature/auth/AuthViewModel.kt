package com.example.vedika.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun updatePhoneNumber(phone: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phone, error = null)
    }

    fun updateOtp(otp: String) {
        _uiState.value = _uiState.value.copy(otp = otp, error = null)
    }

    fun sendOtp(onSuccess: () -> Unit) {
        if (_uiState.value.phoneNumber.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Phone number cannot be empty")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.sendOtp(_uiState.value.phoneNumber)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess { verificationId ->
                _uiState.value = _uiState.value.copy(verificationId = verificationId)
                onSuccess()
            }.onFailure { ex ->
                _uiState.value = _uiState.value.copy(error = ex.message)
            }
        }
    }

    fun verifyOtp(onSuccess: (isNewPartner: Boolean) -> Unit) {
        val verificationId = _uiState.value.verificationId
        if (verificationId == null) {
             _uiState.value = _uiState.value.copy(error = "Invalid session")
             return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.verifyOtp(verificationId, _uiState.value.otp)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess { vendorUser ->
                // Check if primaryServiceCategory is blank (new user onboarding)
                onSuccess(vendorUser.primaryServiceCategory.isBlank())
            }.onFailure { ex ->
                _uiState.value = _uiState.value.copy(error = ex.message)
            }
        }
    }
    
    fun loginAsDevBypass(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.loginAsDevBypass("dev")
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess {
                onSuccess()
            }.onFailure { ex ->
                _uiState.value = _uiState.value.copy(error = ex.message)
            }
        }
    }
}

data class AuthUiState(
    val phoneNumber: String = "",
    val otp: String = "",
    val verificationId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
