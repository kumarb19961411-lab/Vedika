package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.Booking
import com.example.vedika.core.data.model.BookingStatus
import com.example.vedika.core.data.model.SlotType
import com.example.vedika.core.data.model.VendorType
import com.example.vedika.core.data.repository.BookingRepository
import com.example.vedika.core.data.repository.firebase.FirestoreFields
import com.example.vedika.core.data.repository.firebase.FirestorePaths
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FirebaseBookingRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BookingRepository {

    override fun getBookingsForVendor(vendorId: String): Flow<List<Booking>> = callbackFlow {
        val subscription = firestore.collection(FirestorePaths.COL_BOOKINGS)
            .whereEqualTo(FirestoreFields.VENDOR_ID, vendorId)
            .orderBy(FirestoreFields.DATE, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val bookings = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        Booking(
                            id = doc.id,
                            vendorId = doc.getString(FirestoreFields.VENDOR_ID) ?: "",
                            customerName = doc.getString("customerName") ?: "Unknown",
                            eventDate = doc.getLong(FirestoreFields.DATE) ?: 0L,
                            status = BookingStatus.valueOf(doc.getString(FirestoreFields.STATUS) ?: "PENDING"),
                            totalAmount = doc.getDouble("totalAmount") ?: 0.0,
                            slotType = SlotType.valueOf(doc.getString(FirestoreFields.SLOT_TYPE) ?: "FULL_DAY")
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
            firestore.runTransaction { transaction ->
                val dateId = "${booking.vendorId}_${booking.eventDate}"
                val blockRef = firestore.collection(FirestorePaths.COL_BLOCKED_DATES).document(dateId)
                val vendorRef = firestore.collection(FirestorePaths.COL_VENDORS).document(booking.vendorId)
                val occupancyRef = firestore.collection(FirestorePaths.COL_OCCUPANCY).document(dateId)

                // 1. Check for manual blocks
                if (transaction.get(blockRef).exists()) {
                    throw Exception("DATE_BLOCKED: This date has been manually blocked by the vendor.")
                }

                // 2. Fetch Vendor Profile for type and capacity
                val vendorDoc = transaction.get(vendorRef)
                if (!vendorDoc.exists()) throw Exception("VENDOR_NOT_FOUND")
                
                val vendorType = VendorType.valueOf(vendorDoc.getString("vendorType") ?: "VENUE")
                val capacity = vendorDoc.getString("capacity")?.toIntOrNull() ?: 4

                // 3. Check current occupancy
                val occupancyDoc = transaction.get(occupancyRef)
                val currentSlots = (occupancyDoc.get(FirestoreFields.SLOTS_OCCUPIED) as? List<*>)?.map { it.toString() } ?: emptyList()
                val currentJobCount = occupancyDoc.getLong(FirestoreFields.JOB_COUNT) ?: 0L

                // 4. Apply Conflict Logic
                if (vendorType == VendorType.VENUE) {
                    val overlap = when (booking.slotType) {
                        SlotType.FULL_DAY -> currentSlots.isNotEmpty()
                        SlotType.MORNING -> currentSlots.contains(SlotType.FULL_DAY.name) || currentSlots.contains(SlotType.MORNING.name)
                        SlotType.EVENING -> currentSlots.contains(SlotType.FULL_DAY.name) || currentSlots.contains(SlotType.EVENING.name)
                    }
                    if (overlap) throw Exception("SLOT_OCCUPIED: The requested slot overlaps with an existing booking.")
                } else {
                    if (currentJobCount >= capacity) {
                        throw Exception("CAPACITY_FULL: Maximum concurrent jobs reached for this date.")
                    }
                }

                // 5. Commit Writes
                val bookingData = mapOf(
                    FirestoreFields.VENDOR_ID to booking.vendorId,
                    "customerName" to booking.customerName,
                    FirestoreFields.DATE to booking.eventDate,
                    "totalAmount" to booking.totalAmount,
                    FirestoreFields.STATUS to booking.status.name,
                    FirestoreFields.SLOT_TYPE to booking.slotType.name,
                    FirestoreFields.CREATED_AT to com.google.firebase.Timestamp.now()
                )
                
                val bookingRef = firestore.collection(FirestorePaths.COL_BOOKINGS).document()
                transaction.set(bookingRef, bookingData)

                // 6. Update Occupancy Shadow Doc
                val occupancyUpdate = if (occupancyDoc.exists()) {
                    mapOf(
                        FirestoreFields.SLOTS_OCCUPIED to FieldValue.arrayUnion(booking.slotType.name),
                        FirestoreFields.JOB_COUNT to FieldValue.increment(1)
                    )
                } else {
                    mapOf(
                        FirestoreFields.VENDOR_ID to booking.vendorId,
                        FirestoreFields.DATE to booking.eventDate,
                        FirestoreFields.SLOTS_OCCUPIED to listOf(booking.slotType.name),
                        FirestoreFields.JOB_COUNT to 1L
                    )
                }
                transaction.set(occupancyRef, occupancyUpdate, com.google.firebase.firestore.SetOptions.merge())
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateBookingStatus(bookingId: String, status: BookingStatus): Result<Unit> {
        return try {
            firestore.runTransaction { transaction ->
                val bookingRef = firestore.collection(FirestorePaths.COL_BOOKINGS).document(bookingId)
                val bookingDoc = transaction.get(bookingRef)
                if (!bookingDoc.exists()) throw Exception("Booking not found")

                val vendorId = bookingDoc.getString(FirestoreFields.VENDOR_ID) ?: ""
                val eventDate = bookingDoc.getLong(FirestoreFields.DATE) ?: 0L
                val slotType = bookingDoc.getString(FirestoreFields.SLOT_TYPE) ?: ""
                val currentStatus = bookingDoc.getString(FirestoreFields.STATUS) ?: ""

                // 1. Update Booking Status
                transaction.update(bookingRef, FirestoreFields.STATUS, status.name)

                // 2. If transitioning to CANCELLED from a non-cancelled state, release occupancy
                if (status == BookingStatus.CANCELLED && currentStatus != BookingStatus.CANCELLED.name) {
                    val occupancyRef = firestore.collection(FirestorePaths.COL_OCCUPANCY).document("${vendorId}_${eventDate}")
                    transaction.update(occupancyRef, mapOf(
                        FirestoreFields.SLOTS_OCCUPIED to FieldValue.arrayRemove(slotType),
                        FirestoreFields.JOB_COUNT to FieldValue.increment(-1)
                    ))
                }
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun checkConflict(vendorId: String, date: Long, slotType: SlotType): Result<Boolean> {
        return try {
            val occupancyDoc = firestore.collection(FirestorePaths.COL_OCCUPANCY)
                .document("${vendorId}_${date}")
                .get()
                .await()
            
            if (!occupancyDoc.exists()) return Result.success(false)

            // Consult block status too for a complete advisory
            val blockDoc = firestore.collection(FirestorePaths.COL_BLOCKED_DATES)
                .document("${vendorId}_${date}")
                .get()
                .await()
            
            if (blockDoc.exists()) return Result.success(true)

            val currentSlots = (occupancyDoc.get(FirestoreFields.SLOTS_OCCUPIED) as? List<*>)?.map { it.toString() } ?: emptyList()
            
            val hasConflict = when (slotType) {
                SlotType.FULL_DAY -> currentSlots.isNotEmpty()
                SlotType.MORNING -> currentSlots.contains(SlotType.FULL_DAY.name) || currentSlots.contains(SlotType.MORNING.name)
                SlotType.EVENING -> currentSlots.contains(SlotType.FULL_DAY.name) || currentSlots.contains(SlotType.EVENING.name)
            }
            Result.success(hasConflict)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
