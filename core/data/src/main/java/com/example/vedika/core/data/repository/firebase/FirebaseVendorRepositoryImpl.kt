package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.model.VendorMockState
import com.example.vedika.core.data.repository.VendorRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseVendorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : VendorRepository {

    override suspend fun getVendorProfile(vendorId: String): Result<VendorUser> {
        return try {
            val doc = firestore.collection("vendors").document(vendorId).get().await()
            if (doc.exists()) {
                val user = VendorUser(
                    id = doc.id,
                    ownerName = doc.getString("ownerName") ?: "",
                    businessName = doc.getString("businessName") ?: "",
                    primaryServiceCategory = doc.getString("primaryServiceCategory") ?: "",
                    isVerified = doc.getBoolean("isVerified") ?: false
                )
                Result.success(user)
            } else {
                Result.failure(Exception("Vendor profile not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBusinessName(vendorId: String, newName: String): Result<Unit> {
        return try {
            firestore.collection("vendors").document(vendorId)
                .update("businessName", newName).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMockVendor(): Flow<VendorMockState?> {
        // Firebase implementation currently doesn't use mock state, returning empty
        return flowOf(null)
    }

    override suspend fun saveMockVendor(state: VendorMockState): Result<Unit> {
        // Firebase implementation currently doesn't use mock state, returning success
        return Result.success(Unit)
    }
}
