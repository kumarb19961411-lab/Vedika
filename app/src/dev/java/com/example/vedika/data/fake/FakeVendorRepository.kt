package com.example.vedika.data.fake

import com.example.vedika.core.data.model.VendorProfile
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.VendorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeVendorRepository @Inject constructor() : VendorRepository {
    
    private val _vendorProfileState = MutableStateFlow<VendorProfile?>(null)

    override fun getVendorProfileStream(vendorId: String): Flow<VendorProfile?> = _vendorProfileState.asStateFlow()

    override suspend fun saveVendorProfile(profile: VendorProfile): Result<Unit> {
        _vendorProfileState.value = profile
        return Result.success(Unit)
    }
    
    override suspend fun getVendorProfile(vendorId: String): Result<VendorProfile> {
        val current = _vendorProfileState.value
        return Result.success(
            current ?: VendorProfile(
                id = vendorId,
                businessName = "Heritage Halls & Catering",
                ownerName = "Dev Vendor",
                location = "Miyapur, Hyderabad",
                pricing = "₹1,20,000",
                primaryCategory = "Venue",
                vendorType = VendorType.VENUE,
                isVerified = true
            )
        )
    }

    override suspend fun updateBusinessName(vendorId: String, newName: String): Result<Unit> {
        // Just simulate a successful update
        return Result.success(Unit)
    }
}
