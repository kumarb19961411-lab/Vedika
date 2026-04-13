package com.example.vedika.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.VendorRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val vendor: VendorUser? = null,
    val mockVendor: com.example.vedika.core.data.model.VendorMockState? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val vendorRepository: VendorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            // Collect both real user and mock state for data continuity
            launch {
                authRepository.getActiveVendor().collect { vendor ->
                    _uiState.value = _uiState.value.copy(vendor = vendor)
                }
            }
            launch {
                vendorRepository.getMockVendor().collect { mock ->
                    _uiState.value = _uiState.value.copy(mockVendor = mock, isLoading = false)
                }
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            onLoggedOut()
        }
    }
}
