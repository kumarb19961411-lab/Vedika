package com.example.vedika.data.fake

import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {
    private val currentUser = MutableStateFlow<VendorUser?>(null)

    override fun getActiveVendor(): Flow<VendorUser?> = currentUser

    override suspend fun sendOtp(phoneNumber: String): Result<String> {
        kotlinx.coroutines.delay(1500) // Simulate network delay
        if (phoneNumber.isBlank()) return Result.failure(IllegalArgumentException("Invalid phone"))
        return Result.success("mock-verification-id-12345")
    }

    override suspend fun verifyOtp(verificationId: String, otp: String): Result<VendorUser> {
        kotlinx.coroutines.delay(1500) // Simulate network delay
        if (otp != "123456") return Result.failure(IllegalArgumentException("Invalid OTP"))
        
        val devUser = VendorUser(
            id = "mock_vendor_auth",
            businessName = "Heritage Halls",
            ownerName = "Partner Vendor",
            isVerified = false,
            primaryServiceCategory = "Venue"
        )
        currentUser.value = devUser
        return Result.success(devUser)
    }

    override suspend fun loginAsDevBypass(username: String): Result<VendorUser> {
        // Dev bypass hardcoded user logic
        val devUser = VendorUser(
            id = "dev_vendor_001",
            businessName = "Heritage Halls & Catering",
            ownerName = "Dev Vendor",
            isVerified = true,
            primaryServiceCategory = "Venue"
        )
        currentUser.value = devUser
        return Result.success(devUser)
    }

    override suspend fun logout() {
        currentUser.value = null
    }
}
