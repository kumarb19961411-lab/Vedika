package com.example.vedika.data.fake

import com.example.vedika.core.data.model.VendorMockState
import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.repository.VendorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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
        val mock = _mockVendorState.value
        return Result.success(
            VendorUser(
                id = vendorId,
                businessName = mock?.venueName ?: "Heritage Halls & Catering",
                ownerName = "Dev Vendor",
                isVerified = true,
                primaryServiceCategory = mock?.vendorType?.name ?: "Venue"
            )
        )
    }

    override fun getVendorProfileStream(vendorId: String): Flow<VendorUser?> {
        return _mockVendorState.map { mock ->
            VendorUser(
                id = vendorId,
                businessName = mock?.venueName ?: "Heritage Halls & Catering",
                ownerName = "Dev Vendor",
                isVerified = true,
                primaryServiceCategory = mock?.vendorType?.name ?: "Venue"
            )
        }
    }

    override suspend fun updateBusinessName(vendorId: String, newName: String): Result<Unit> {
        // Just simulate a successful update
        return Result.success(Unit)
    }
}
