package com.example.vedika.core.data.model

enum class AccountMode { USER, PARTNER }
enum class AuthFlow { SIGN_IN, SIGN_UP }

sealed class RoleResolutionState {
    object Idle : RoleResolutionState()
    object Loading : RoleResolutionState()
    object OtpSent : RoleResolutionState()
    data class Verified(val uid: String, val profileExists: Boolean = false) : RoleResolutionState()
    object AccountNotFound : RoleResolutionState()
    data class Error(val message: String) : RoleResolutionState()
}
