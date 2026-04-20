package com.example.vedika.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.AccountMode
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.UserRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.session.SessionStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class StartupState {
    object Idle : StartupState()
    object Loading : StartupState()
    object Unauthenticated : StartupState()
    data class Authenticated(val mode: AccountMode, val profileExists: Boolean) : StartupState()
    data class Error(val message: String, val isNetworkError: Boolean = false) : StartupState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val vendorRepository: VendorRepository,
    private val userRepository: UserRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val _startupState = MutableStateFlow<StartupState>(StartupState.Idle)
    val startupState: StateFlow<StartupState> = _startupState.asStateFlow()

    init {
        resolveUserSession()
    }

    fun resolveUserSession() {
        viewModelScope.launch {
            _startupState.value = StartupState.Loading
            
            val uid = authRepository.getCurrentUserId()
            if (uid == null) {
                _startupState.value = StartupState.Unauthenticated
                return@launch
            }

            val modeHint = sessionStorage.getAccountMode()
            if (modeHint == null) {
                // If we have an auth session but no mode hint, we fallback to Auth selection
                // This could happen on upgrade or data clear
                _startupState.value = StartupState.Unauthenticated
                return@launch
            }

            try {
                if (modeHint == AccountMode.PARTNER) {
                    vendorRepository.getVendorProfile(uid)
                        .onSuccess {
                            _startupState.value = StartupState.Authenticated(modeHint, profileExists = true)
                        }
                        .onFailure { ex ->
                            if (ex.message == "VENDOR_NOT_FOUND") {
                                _startupState.value = StartupState.Authenticated(modeHint, profileExists = false)
                            } else {
                                _startupState.value = StartupState.Error(
                                    message = "Failed to load partner profile.",
                                    isNetworkError = true // Simplified for now
                                )
                            }
                        }
                } else {
                    userRepository.getUserProfile(uid)
                        .onSuccess {
                            _startupState.value = StartupState.Authenticated(modeHint, profileExists = true)
                        }
                        .onFailure { ex ->
                            if (ex.message == "USER_NOT_FOUND") {
                                _startupState.value = StartupState.Authenticated(modeHint, profileExists = false)
                            } else {
                                _startupState.value = StartupState.Error(
                                    message = "Failed to load user profile.",
                                    isNetworkError = true
                                )
                            }
                        }
                }
            } catch (e: Exception) {
                _startupState.value = StartupState.Error(
                    message = e.message ?: "An unexpected error occurred during startup.",
                    isNetworkError = true
                )
            }
        }
    }
}
