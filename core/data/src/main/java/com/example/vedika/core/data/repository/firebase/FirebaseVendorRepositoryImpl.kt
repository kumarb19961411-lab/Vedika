package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.VendorProfile
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.model.PackageTier
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.repository.firebase.FirestorePaths
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseVendorRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : VendorRepository {

    override suspend fun getVendorProfile(vendorId: String): Result<VendorProfile> {
        return try {
            val doc = firestore.collection(FirestorePaths.COL_VENDORS).document(vendorId).get().await()
            if (doc.exists()) {
                Result.success(mapDocumentToProfile(doc.id, doc.data ?: emptyMap()))
            } else {
                Result.failure(Exception("VENDOR_NOT_FOUND"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    private fun mapDocumentToProfile(id: String, map: Map<String, Any>): VendorProfile {
        return VendorProfile(
            id = id,
            businessName = map["businessName"] as? String ?: "",
            ownerName = map["ownerName"] as? String ?: "",
            location = map["location"] as? String ?: "",
            pricing = map["pricing"] as? String ?: "",
            primaryCategory = map["primaryServiceCategory"] as? String ?: "",
            vendorType = try { 
                VendorType.valueOf(map["vendorType"] as? String ?: "VENUE") 
            } catch (e: Exception) { 
                VendorType.VENUE 
            },
            isVerified = map["isVerified"] as? Boolean ?: false,
            capacity = map["capacity"] as? String,
            amenities = map["amenities"] as? List<String> ?: emptyList(),
            coverImage = map["coverImage"] as? String,
            galleryImages = map["galleryImages"] as? List<String> ?: emptyList(),
            yearsExperience = map["yearsExperience"] as? String,
            packageTiers = (map["packageTiers"] as? List<Map<String, Any>>)?.map {
                PackageTier(
                    name = it["name"] as? String ?: "",
                    price = it["price"] as? String ?: "",
                    inclusions = it["inclusions"] as? String ?: ""
                )
            } ?: emptyList(),
            rating = map["rating"] as? String,
            leadsCount = map["leadsCount"] as? String,
            area = map["area"] as? String,
            venueType = map["venueType"] as? String,
            featuredAssetTitle = map["featuredAssetTitle"] as? String,
            featuredAssetImage = map["featuredAssetImage"] as? String,
            featuredAssetPrice = map["featuredAssetPrice"] as? String
        )
    }

    override fun getVendorProfileStream(vendorId: String): Flow<VendorProfile?> = callbackFlow {
        val subscription = firestore.collection(FirestorePaths.COL_VENDORS).document(vendorId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                if (snapshot != null && snapshot.exists()) {
                    trySend(mapDocumentToProfile(snapshot.id, snapshot.data ?: emptyMap()))
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

    override suspend fun saveVendorProfile(profile: VendorProfile): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("AUTH_REQUIRED: User must be signed in to register.")
            val data = mutableMapOf(
                "ownerName" to profile.ownerName,
                "businessName" to profile.businessName,
                "primaryServiceCategory" to profile.primaryCategory,
                "location" to profile.location,
                "capacity" to profile.capacity.orEmpty(),
                "pricing" to profile.pricing,
                "amenities" to profile.amenities,
                "vendorType" to profile.vendorType.name,
                "isVerified" to profile.isVerified,
                "yearsExperience" to profile.yearsExperience.orEmpty(),
                "coverImage" to profile.coverImage.orEmpty(),
                "galleryImages" to profile.galleryImages,
                "packageTiers" to profile.packageTiers.map {
                    mapOf(
                        "name" to it.name,
                        "price" to it.price,
                        "inclusions" to it.inclusions
                    )
                }
            )
            
            // Persist read-only dashboard fields if they exist (for data continuity)
            profile.rating?.let { data["rating"] = it }
            profile.leadsCount?.let { data["leadsCount"] = it }
            profile.area?.let { data["area"] = it }
            profile.venueType?.let { data["venueType"] = it }
            profile.featuredAssetTitle?.let { data["featuredAssetTitle"] = it }
            profile.featuredAssetImage?.let { data["featuredAssetImage"] = it }
            profile.featuredAssetPrice?.let { data["featuredAssetPrice"] = it }

            firestore.collection(FirestorePaths.COL_VENDORS).document(uid).set(data).await()
            Result.success(Unit) 
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getVendorsByCategory(category: String): Flow<List<VendorProfile>> = callbackFlow {
        val query = firestore.collection(FirestorePaths.COL_VENDORS)
            .whereEqualTo("primaryServiceCategory", category)
        
        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.map { doc ->
                    mapDocumentToProfile(doc.id, doc.data ?: emptyMap())
                }
                trySend(list)
            }
        }
        awaitClose { subscription.remove() }
    }

    override fun getFeaturedVendors(): Flow<List<VendorProfile>> = callbackFlow {
        val query = firestore.collection(FirestorePaths.COL_VENDORS)
            .whereEqualTo("isVerified", true)
            .limit(10)
        
        val subscription = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val list = snapshot.documents.map { doc ->
                    mapDocumentToProfile(doc.id, doc.data ?: emptyMap())
                }
                trySend(list)
            }
        }
        awaitClose { subscription.remove() }
    }
}
