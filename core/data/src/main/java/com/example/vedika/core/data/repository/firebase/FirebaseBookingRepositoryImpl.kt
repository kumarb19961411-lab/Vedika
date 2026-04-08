package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.repository.BookingRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class FirebaseBookingRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BookingRepository {

    override fun getBookingsForVendor(vendorId: String): Flow<List<Booking>> = callbackFlow {
        val subscription = firestore.collection("bookings")
            .whereEqualTo("vendorId", vendorId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val bookings = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Booking(
                            id = doc.id,
                            vendorId = doc.getString("vendorId") ?: "",
                            customerName = doc.getString("customerName") ?: "Unknown",
                            eventDate = doc.getLong("eventDate") ?: 0L,
                            status = BookingStatus.valueOf(doc.getString("status") ?: "PENDING"),
                            totalAmount = doc.getDouble("totalAmount") ?: 0.0
                        )
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()
                
                trySend(bookings)
            }
            
        awaitClose { subscription.remove() }
    }

    override suspend fun createBooking(booking: Booking): Result<Unit> {
        return try {
            val data = mapOf(
                "vendorId" to booking.vendorId,
                "customerName" to booking.customerName,
                "eventDate" to booking.eventDate,
                "totalAmount" to booking.totalAmount,
                "status" to booking.status.name
            )
            // Use custom ID if provided or generate new document
            if (booking.id.isNotEmpty()) {
                firestore.collection("bookings").document(booking.id).set(data).await()
            } else {
                firestore.collection("bookings").add(data).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Result<Unit> {
        // SECURITY & DOMAIN DEFERRED: 
        // Direct mutations are generally unsafe for Payouts. 
        // This is a client-side execution that will be replaced by a Firebase Function in the Prod backend.
        return try {
            firestore.collection("bookings").document(bookingId)
                .update("status", status.name).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
