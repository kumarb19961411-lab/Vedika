package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.model.VendorMockState
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.repository.firebase.FirestorePaths
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseVendorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : VendorRepository {

    override suspend fun getVendorProfile(vendorId: String): Result<VendorUser> {
        return try {
            val doc = firestore.collection(FirestorePaths.COL_VENDORS).document(vendorId).get().await()
            if (doc.exists()) {
                Result.success(
                    VendorUser(
                        id = doc.id,
                        businessName = doc.getString("businessName") ?: "",
                        ownerName = doc.getString("ownerName") ?: "",
                        isVerified = doc.getBoolean("isVerified") ?: false,
                        primaryServiceCategory = doc.getString("primaryServiceCategory") ?: ""
                    )
                )
            } else {
                Result.failure(Exception("VENDOR_NOT_FOUND"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getVendorProfileStream(vendorId: String): Flow<VendorUser?> = callbackFlow {
        val subscription = firestore.collection(FirestorePaths.COL_VENDORS).document(vendorId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null && snapshot.exists()) {
                    val user = VendorUser(
                        id = snapshot.id,
                        ownerName = snapshot.getString("ownerName") ?: "",
                        businessName = snapshot.getString("businessName") ?: "",
                        primaryServiceCategory = snapshot.getString("primaryServiceCategory") ?: "",
                        isVerified = snapshot.getBoolean("isVerified") ?: false
                    )
                    trySend(user)
                } else {
                    trySend(null)
                }
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun updateBusinessName(vendorId: String, newName: String): Result<Unit> {
        return try {
            firestore.collection(FirestorePaths.COL_VENDORS).document(vendorId)
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
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("AUTH_REQUIRED: User must be signed in to register.")
            val data = mapOf(
                "ownerName" to state.ownerName,
                "businessName" to state.businessName,
                "primaryServiceCategory" to state.primaryCategory,
                "location" to state.location,
                "capacity" to state.capacity.orEmpty().ifBlank { "4" }, // Fallback to 4 for decorators
                "pricing" to state.pricing,
                "amenities" to state.amenities,
                "vendorType" to state.vendorType.name,
                "isVerified" to false
            )
            
            firestore.collection(FirestorePaths.COL_VENDORS).document(uid).set(data).await()
            Result.success(Unit) 
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
