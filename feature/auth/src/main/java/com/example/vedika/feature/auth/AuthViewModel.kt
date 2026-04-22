package com.example.vedika.feature.auth

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vedika.core.data.model.AccountMode
import com.example.vedika.core.data.model.AuthFlow
import com.example.vedika.core.data.model.RoleResolutionState
import com.example.vedika.core.data.model.AppUser
import com.example.vedika.core.data.model.VendorProfile
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.UserRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.session.SessionStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val vendorRepository: VendorRepository,
    private val userRepository: UserRepository,
    private val sessionStorage: SessionStorage,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _pendingDestination = savedStateHandle.getStateFlow<String?>("pendingDestination", null)
    val pendingDestination: StateFlow<String?> = _pendingDestination

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        logDebug("Initializing reactive profile observation...")
        viewModelScope.launch {
            authRepository.getActiveVendor().collect { vendor ->
                if (vendor != null) {
                    logDebug("Hydrated Vendor found: ${vendor.ownerName} - ${vendor.primaryServiceCategory}")
                    _uiState.update { it.copy(
                        ownerName = vendor.ownerName,
                        selectedCategory = vendor.primaryServiceCategory
                    ) }
                }
            }
        }
    }

    private fun logDebug(message: String) {
        Log.d("VedikaDebug", "AuthViewModel: $message")
    }

    fun selectAccountMode(mode: AccountMode) {
        logDebug("Account Mode selected: $mode")
        _uiState.value = _uiState.value.copy(accountMode = mode, error = null)
    }

    fun setAuthFlow(flow: AuthFlow) {
        logDebug("Auth Flow set: $flow")
        _uiState.value = _uiState.value.copy(authFlow = flow, error = null)
    }

    fun updatePhoneNumber(phone: String) {
        _uiState.value = _uiState.value.copy(phoneNumber = phone, error = null)
    }

    fun updateOtp(otp: String) {
        if (otp.length <= 6) {
            _uiState.value = _uiState.value.copy(otp = otp, error = null)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun sendOtp(activity: android.app.Activity, onSuccess: () -> Unit) {
        val state = _uiState.value
        // For Partner Sign-Up, the owner name must be provided before sending OTP
        if (state.authFlow == AuthFlow.SIGN_UP && state.accountMode == AccountMode.PARTNER
            && state.ownerName.isBlank()) {
            _uiState.value = state.copy(error = "Please enter your full name to continue")
            return
        }
        
        // Format phone number to E.164 (+91 prefix for local 10-digit numbers)
        val rawPhone = state.phoneNumber.trim()
        val formattedPhone = when {
            rawPhone.startsWith("+") -> rawPhone
            rawPhone.length == 10 -> "+91$rawPhone"
            else -> rawPhone // Fallback to raw if not 10 digits/prefixed
        }

        if (formattedPhone.length < 12) { // +91 + 10 digits = 13 chars (but let's be safe)
            _uiState.value = state.copy(error = "Please enter a valid 10-digit phone number")
            return
        }

        logDebug("Triggering sendOtp for: $formattedPhone (Raw: $rawPhone)")

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                roleResolutionState = RoleResolutionState.Loading
            )
            val startTime = System.currentTimeMillis()
            logDebug("SEND_OTP_START: Requesting OTP for $formattedPhone")
            val result = authRepository.sendOtp(formattedPhone, activity)
            val duration = System.currentTimeMillis() - startTime
            logDebug("SEND_OTP_RETURNED: Repository call took ${duration}ms")
            
            _uiState.value = _uiState.value.copy(isLoading = false)
            result.onSuccess { verificationId ->
                logDebug("SEND_OTP_SUCCESS: VerificationId: $verificationId")
                _uiState.value = _uiState.value.copy(
                    verificationId = verificationId,
                    isResendEnabled = false,
                    countdown = 30,
                    roleResolutionState = RoleResolutionState.OtpSent
                )
                startCountdown()
                onSuccess()
            }.onFailure { ex ->
                logDebug("OTP Send FAILED: ${ex.message}")
                _uiState.value = _uiState.value.copy(
                    error = ex.message,
                    roleResolutionState = RoleResolutionState.Error(ex.message ?: "Failed to send OTP")
                )
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

    fun resendOtp(activity: android.app.Activity) {
        if (!_uiState.value.isResendEnabled) return
        sendOtp(activity) { /* No-op success callback for resend */ }
    }

    fun verifyOtp(onSuccess: () -> Unit) {
        if (_uiState.value.otp.length < 6) {
            _uiState.value = _uiState.value.copy(error = "Enter 6-digit OTP")
            return
        }
        val verificationId = _uiState.value.verificationId ?: "mock-session"
        logDebug("VERIFY_OTP_START: OTP=${_uiState.value.otp}, Session=$verificationId")
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                roleResolutionState = RoleResolutionState.Loading
            )
            val startTime = System.currentTimeMillis()
            val result = authRepository.verifyOtp(verificationId, _uiState.value.otp)
            val duration = System.currentTimeMillis() - startTime
            logDebug("VERIFY_OTP_RETURNED: Repository call took ${duration}ms")

            result.onSuccess { user ->
                logDebug("VERIFY_OTP_SUCCESS: Uid=${user.id}")
                if (_uiState.value.accountMode == AccountMode.PARTNER) {
                    logDebug("PROFILE_FETCH_START: Checking Vendor Profile...")
                    val profileResult = vendorRepository.getVendorProfile(user.id)
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    profileResult.onSuccess {
                        logDebug("STATE_UPDATE: RoleResolutionState -> Verified(profileExists=true)")
                        _uiState.value = _uiState.value.copy(
                            roleResolutionState = RoleResolutionState.Verified(user.id, profileExists = true)
                        )
                        onSuccess()
                    }.onFailure { ex ->
                        if (ex.message == "VENDOR_NOT_FOUND") {
                            logDebug("PROFILE_FETCH_NOT_FOUND: No vendor profile yet.")
                            sessionStorage.saveAccountMode(_uiState.value.accountMode)
                            logDebug("STATE_UPDATE: RoleResolutionState -> Verified(profileExists=false)")
                            _uiState.value = _uiState.value.copy(
                                roleResolutionState = RoleResolutionState.Verified(user.id, profileExists = false)
                            )
                            onSuccess()
                        } else {
                            logDebug("PROFILE_FETCH_FAILED: ${ex.message}")
                            _uiState.value = _uiState.value.copy(
                                error = "Failed to load profile. Please try again.",
                                roleResolutionState = RoleResolutionState.Error("Failed to load profile.")
                            )
                        }
                    }
                } else {
                    logDebug("PROFILE_FETCH_START: Checking User Profile...")
                    val profileResult = userRepository.getUserProfile(user.id)
                    profileResult.onSuccess {
                        logDebug("PROFILE_FETCH_SUCCESS: User profile exists.")
                        sessionStorage.saveAccountMode(_uiState.value.accountMode)
                        _uiState.update { it.copy(
                            isLoading = false,
                            roleResolutionState = RoleResolutionState.Verified(user.id, profileExists = true)
                        ) }
                        onSuccess()
                    }.onFailure { ex ->
                        if (ex.message == "USER_NOT_FOUND") {
                            if (_uiState.value.authFlow == AuthFlow.SIGN_IN) {
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    error = "No user profile found. Please register first.",
                                    roleResolutionState = RoleResolutionState.AccountNotFound
                                ) }
                            } else {
                                val newUser = AppUser(
                                    uid = user.id,
                                    fullName = _uiState.value.ownerName.takeIf { it.isNotBlank() } ?: "User",
                                    phoneNumber = _uiState.value.phoneNumber
                                )
                                viewModelScope.launch {
                                    val createResult = userRepository.createUserProfile(newUser)
                                    _uiState.update { it.copy(isLoading = false) }
                                    createResult.onSuccess {
                                        sessionStorage.saveAccountMode(_uiState.value.accountMode)
                                        _uiState.update { it.copy(
                                            roleResolutionState = RoleResolutionState.Verified(user.id, profileExists = true)
                                        ) }
                                        onSuccess()
                                    }.onFailure { createEx ->
                                        _uiState.update { it.copy(
                                            error = "Failed to create profile: ${createEx.message}",
                                            roleResolutionState = RoleResolutionState.Error("Failed to create profile.")
                                        ) }
                                    }
                                }
                            }
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = "Failed to load user profile. Please try again.",
                                roleResolutionState = RoleResolutionState.Error("Failed to load user profile.")
                            ) }
                        }
                    }
                }
            }.onFailure { ex ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = ex.message ?: "Invalid OTP",
                    roleResolutionState = RoleResolutionState.Error(ex.message ?: "Invalid OTP")
                )
            }
        }
    }
    
    fun loginAsDevBypass(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true, 
                error = null,
                roleResolutionState = RoleResolutionState.Loading
            )
            val result = authRepository.loginAsDevBypass("dev")
            result.onSuccess { user ->
                if (_uiState.value.accountMode == AccountMode.PARTNER) {
                    val profileResult = vendorRepository.getVendorProfile(user.id)
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    profileResult.onSuccess {
                        sessionStorage.saveAccountMode(_uiState.value.accountMode)
                        logDebug("STATE_UPDATE: RoleResolutionState -> Verified(profileExists=true)")
                        _uiState.value = _uiState.value.copy(
                            roleResolutionState = RoleResolutionState.Verified(user.id, profileExists = true)
                        )
                        onSuccess()
                    }.onFailure { ex ->
                        if (ex.message == "VENDOR_NOT_FOUND") {
                            logDebug("DEV_BYPASS: PROFILE_FETCH_NOT_FOUND: No vendor profile yet.")
                            sessionStorage.saveAccountMode(_uiState.value.accountMode)
                            logDebug("STATE_UPDATE: RoleResolutionState -> Verified(profileExists=false)")
                            _uiState.value = _uiState.value.copy(
                                roleResolutionState = RoleResolutionState.Verified(user.id, profileExists = false)
                            )
                            onSuccess()
                        } else {
                            _uiState.value = _uiState.value.copy(
                                error = "Failed to load profile. Please try again.",
                                roleResolutionState = RoleResolutionState.Error("Failed to load profile.")
                            )
                        }
                    }
                } else {
                    val profileResult = userRepository.getUserProfile(user.id)
                    profileResult.onSuccess {
                        sessionStorage.saveAccountMode(_uiState.value.accountMode)
                        _uiState.update { it.copy(
                            isLoading = false,
                            roleResolutionState = RoleResolutionState.Verified(user.id, profileExists = true)
                        ) }
                        onSuccess()
                    }.onFailure { ex ->
                        if (ex.message == "USER_NOT_FOUND") {
                            if (_uiState.value.authFlow == AuthFlow.SIGN_IN) {
                                _uiState.update { it.copy(
                                    isLoading = false,
                                    error = "No user profile found. Please register first.",
                                    roleResolutionState = RoleResolutionState.AccountNotFound
                                ) }
                            } else {
                                val newUser = AppUser(
                                    uid = user.id,
                                    fullName = _uiState.value.ownerName.takeIf { it.isNotBlank() } ?: "User",
                                    phoneNumber = _uiState.value.phoneNumber.takeIf { it.isNotBlank() } ?: "0000000000"
                                )
                                viewModelScope.launch {
                                    val createResult = userRepository.createUserProfile(newUser)
                                    _uiState.update { it.copy(isLoading = false) }
                                    createResult.onSuccess {
                                        sessionStorage.saveAccountMode(_uiState.value.accountMode)
                                        _uiState.update { it.copy(
                                            roleResolutionState = RoleResolutionState.Verified(user.id, profileExists = true)
                                        ) }
                                        onSuccess()
                                    }.onFailure { createEx ->
                                        _uiState.update { it.copy(
                                            error = "Failed to create profile: ${createEx.message}",
                                            roleResolutionState = RoleResolutionState.Error("Failed to create profile.")
                                        ) }
                                    }
                                }
                            }
                        } else {
                            _uiState.update { it.copy(
                                isLoading = false,
                                error = "Failed to load user profile. Please try again.",
                                roleResolutionState = RoleResolutionState.Error("Failed to load user profile.")
                            ) }
                        }
                    }
                }
            }.onFailure { ex ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = ex.message
                )
            }
        }
    }

    fun updateSelectedCategory(category: String?) {
        logDebug("Category selected: $category")
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun updateOwnerName(name: String) {
        _uiState.value = _uiState.value.copy(ownerName = name)
    }

    /**
     * Allows the CategorySelectionScreen to capture a contact name for Partners
     * who arrived via Sign-In (skipping SignupScreen) and thus have no ownerName.
     */
    fun updateOwnerNameForRegistration(name: String) {
        _uiState.value = _uiState.value.copy(ownerName = name)
    }

    // Venue Registration State Updates
    fun updateVenueName(name: String) { _uiState.value = _uiState.value.copy(venueName = name) }
    fun updateVenueCapacity(capacity: String) { _uiState.value = _uiState.value.copy(venueCapacity = capacity) }
    fun updateVenueLocation(location: String) { _uiState.value = _uiState.value.copy(venueLocation = location) }
    fun updateVenuePrice(price: String) { _uiState.value = _uiState.value.copy(venuePrice = price) }
    fun updateVenueAmenities(amenity: String) {
        val current = _uiState.value.venueAmenities.toMutableList()
        if (current.contains(amenity)) current.remove(amenity) else current.add(amenity)
        _uiState.value = _uiState.value.copy(venueAmenities = current)
    }

    // Decorator Registration State Updates
    fun updateDecoratorBusinessName(name: String) { _uiState.value = _uiState.value.copy(decoratorBusinessName = name) }
    fun updateDecoratorExperience(exp: String) { _uiState.value = _uiState.value.copy(decoratorExperience = exp) }
    fun updateDecoratorExpertise(expertise: String) {
        val current = _uiState.value.decoratorExpertise.toMutableList()
        if (current.contains(expertise)) current.remove(expertise) else current.add(expertise)
        _uiState.value = _uiState.value.copy(decoratorExpertise = current)
    }
    fun updateDecoratorTier1Price(price: String) { _uiState.value = _uiState.value.copy(decoratorTier1Price = price) }
    fun updateDecoratorTier1Inclusions(inc: String) { _uiState.value = _uiState.value.copy(decoratorTier1Inclusions = inc) }
    fun updateDecoratorTier2Price(price: String) { _uiState.value = _uiState.value.copy(decoratorTier2Price = price) }
    fun updateDecoratorTier2Inclusions(inc: String) { _uiState.value = _uiState.value.copy(decoratorTier2Inclusions = inc) }

    fun saveRegistrationData(onSuccess: () -> Unit) {
        val state = _uiState.value
        val vendorId = authRepository.getCurrentUserId() ?: return
        val vendorType = if (state.selectedCategory?.lowercase()?.contains("venue") == true) VendorType.VENUE else VendorType.DECORATOR
        
        val profile = if (vendorType == VendorType.VENUE) {
            VendorProfile(
                id = vendorId,
                businessName = state.venueName,
                ownerName = state.ownerName,
                location = state.venueLocation,
                pricing = state.venuePrice,
                primaryCategory = state.selectedCategory ?: "Venue",
                vendorType = VendorType.VENUE,
                capacity = state.venueCapacity,
                amenities = state.venueAmenities,
                coverImage = "https://images.unsplash.com/photo-1519167758481-83f550bb49b3", // High-fidelity Venue placeholder
                area = "12,000 Sq Ft", 
                venueType = "Indoor/Outdoor",
                rating = "4.8",
                leadsCount = "8 New"
            )
        } else {
            val tiers = listOf(
                com.example.vedika.core.data.model.PackageTier(
                    name = "Essential Tier",
                    price = state.decoratorTier1Price,
                    inclusions = state.decoratorTier1Inclusions.ifBlank { "Base setup, entry decor, and lighting" }
                ),
                com.example.vedika.core.data.model.PackageTier(
                    name = "Signature Heritage",
                    price = state.decoratorTier2Price,
                    inclusions = state.decoratorTier2Inclusions.ifBlank { "Premium floral mandap, stage setup, and thematic entry" }
                )
            )

            VendorProfile(
                id = vendorId,
                businessName = state.decoratorBusinessName,
                ownerName = state.ownerName,
                location = "Main Market, Hyderabad", // Generic location for decorators
                pricing = state.decoratorTier1Price,
                primaryCategory = state.selectedCategory ?: "Decorator",
                vendorType = VendorType.DECORATOR,
                yearsExperience = state.decoratorExperience,
                packageTiers = tiers,
                amenities = state.decoratorExpertise,
                coverImage = "https://images.unsplash.com/photo-1511795409834-ef04bbd61622", // High-fidelity Decorator placeholder
                rating = "4.9",
                leadsCount = "12 New"
            )
        }

        logDebug("Saving Registration Profile for: $vendorType (Owner: ${state.ownerName})")

        // Hard guard: ownerName must be present. The sendOtp and CategorySelection
        // steps should have already captured this, but we validate here as a safety net.
        if (state.ownerName.isBlank()) {
            _uiState.value = _uiState.value.copy(
                error = "Contact name is required. Please go back and enter your name."
            )
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            vendorRepository.saveVendorProfile(profile)
                .onSuccess {
                    logDebug("Registration Profile saved successfully.")
                    _uiState.value = _uiState.value.copy(isLoading = false)
                    onSuccess()
                }
                .onFailure { ex ->
                    logDebug("Registration failed: ${ex.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = ex.message ?: "Failed to save registration. Please try again."
                    )
                }
        }
    }

    fun setPendingDestination(destination: String?) {
        logDebug("Setting pending destination: $destination")
        savedStateHandle["pendingDestination"] = destination
    }

    fun clearPendingDestination() {
        logDebug("Clearing pending destination")
        savedStateHandle["pendingDestination"] = null
    }
}

data class AuthUiState(
    val phoneNumber: String = "",
    val otp: String = "",
    val accountMode: AccountMode = AccountMode.PARTNER,
    val authFlow: AuthFlow = AuthFlow.SIGN_IN,
    val verificationId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isResendEnabled: Boolean = true,
    val countdown: Int = 0,
    val selectedCategory: String? = null,
    val ownerName: String = "",
    val roleResolutionState: RoleResolutionState = RoleResolutionState.Idle,
    
    // Venue Registration Form State
    val venueName: String = "",
    val venueCapacity: String = "",
    val venueLocation: String = "",
    val venuePrice: String = "",
    val venueAmenities: List<String> = emptyList(),
    
    // Decorator Registration Form State
    val decoratorBusinessName: String = "",
    val decoratorExperience: String = "3-7 Years",
    val decoratorExpertise: List<String> = emptyList(),
    val decoratorTier1Price: String = "",
    val decoratorTier1Inclusions: String = "",
    val decoratorTier2Price: String = "",
    val decoratorTier2Inclusions: String = ""
)
