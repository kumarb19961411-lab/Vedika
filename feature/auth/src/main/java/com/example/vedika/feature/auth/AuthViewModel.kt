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

enum class LoginMode { USER, VENDOR }

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun selectLoginMode(mode: LoginMode) {
        _uiState.value = _uiState.value.copy(loginMode = mode, error = null)
    }

    fun updatePhoneNumber(phone: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phone, error = null)
    }

    fun updateOtp(otp: String) {
        if (otp.length <= 4) {
            _uiState.value = _uiState.value.copy(otp = otp, error = null)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun sendOtp(onSuccess: () -> Unit) {
        if (_uiState.value.phoneNumber.length < 10) {
            _uiState.value = _uiState.value.copy(error = "Please enter a valid 10-digit phone number")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.sendOtp(_uiState.value.phoneNumber)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess { verificationId ->
                _uiState.value = _uiState.value.copy(
                    verificationId = verificationId,
                    isResendEnabled = false,
                    countdown = 30
                )
                startCountdown()
                onSuccess()
            }.onFailure { ex ->
                _uiState.value = _uiState.value.copy(error = ex.message)
            }
        }
    }

    private fun startCountdown() {
        viewModelScope.launch {
            while (_uiState.value.countdown > 0) {
                kotlinx.coroutines.delay(1000)
                _uiState.value = _uiState.value.copy(countdown = _uiState.value.countdown - 1)
            }
            _uiState.value = _uiState.value.copy(isResendEnabled = true)
        }
    }

    fun resendOtp() {
        if (!_uiState.value.isResendEnabled) return
        sendOtp { /* No-op success callback for resend */ }
    }

    fun verifyOtp(onSuccess: (isNewPartner: Boolean) -> Unit) {
        if (_uiState.value.otp.length < 4) {
            _uiState.value = _uiState.value.copy(error = "Enter 4-digit OTP")
            return
        }
        val verificationId = _uiState.value.verificationId ?: "mock-session" // Fallback for dev mode
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.verifyOtp(verificationId, _uiState.value.otp)
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess { vendorUser ->
                onSuccess(vendorUser.primaryServiceCategory.isBlank())
            }.onFailure { ex ->
                _uiState.value = _uiState.value.copy(error = ex.message ?: "Invalid OTP")
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
    val loginMode: LoginMode = LoginMode.VENDOR,
    val verificationId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isResendEnabled: Boolean = true,
    val countdown: Int = 0
)
