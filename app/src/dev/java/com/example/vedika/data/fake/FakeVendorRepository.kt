package com.example.vedika.data.fake

import com.example.vedika.core.data.model.VendorMockState
import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.repository.VendorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeVendorRepository @Inject constructor() : VendorRepository {
    
    private val _mockVendorState = MutableStateFlow<VendorMockState?>(null)

    override fun getMockVendor(): Flow<VendorMockState?> = _mockVendorState.asStateFlow()

    override suspend fun saveMockVendor(state: VendorMockState): Result<Unit> {
        _mockVendorState.value = state
        return Result.success(Unit)
    }
    
    override suspend fun getVendorProfile(vendorId: String): Result<VendorUser> {
        return Result.success(
            VendorUser(
                id = vendorId,
                businessName = "Heritage Halls & Catering",
                ownerName = "Dev Vendor",
                isVerified = true,
                primaryServiceCategory = "Venue"
            )
        )
    }

    override suspend fun updateBusinessName(vendorId: String, newName: String): Result<Unit> {
        // Just simulate a successful update
        return Result.success(Unit)
    }
}
