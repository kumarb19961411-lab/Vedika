package com.example.vedika.data.fake

import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.repository.VendorRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeVendorRepository @Inject constructor() : VendorRepository {
    
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
