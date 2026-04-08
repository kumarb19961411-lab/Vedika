package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.InventoryItem
import com.example.vedika.core.data.repository.InventoryRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseInventoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : InventoryRepository {

    override fun getInventoryForVendor(vendorId: String): Flow<List<InventoryItem>> = callbackFlow {
        val subscription = firestore.collection("inventory")
            .whereEqualTo("vendorId", vendorId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val items = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        InventoryItem(
                            id = doc.id,
                            vendorId = doc.getString("vendorId") ?: "",
                            name = doc.getString("name") ?: "",
                            description = doc.getString("description") ?: "",
                            price = doc.getDouble("price") ?: 0.0,
                            isAvailable = doc.getBoolean("isAvailable") ?: true
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()
                
                trySend(items)
            }
            
        awaitClose { subscription.remove() }
    }

    override suspend fun addInventoryItem(item: InventoryItem): Result<Unit> {
        return try {
            val data = mapOf(
                "vendorId" to item.vendorId, // using actual vendorId now that Model allows it
                "name" to item.name,
                "description" to item.description,
                "price" to item.price,
                "isAvailable" to item.isAvailable
            )
            firestore.collection("inventory").add(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateInventoryAvailability(itemId: String, isAvailable: Boolean): Result<Unit> {
        return try {
            firestore.collection("inventory").document(itemId)
                .update("isAvailable", isAvailable).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
