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

    companion object {
        private const val DEFAULT_CAPACITY_VENUE = 1
        private const val DEFAULT_CAPACITY_DECORATOR = 4
        private const val TAG = "FirebaseBookingRepo"
    }

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
                    throw Exception("CONFLICT_DATE_BLOCKED")
                }

                // 2. Fetch Vendor Profile for type and capacity
                val vendorDoc = transaction.get(vendorRef)
                if (!vendorDoc.exists()) throw Exception("ERROR_VENDOR_NOT_FOUND")
                
                val vendorTypeStr = vendorDoc.getString("vendorType") ?: vendorDoc.getString("primaryServiceCategory") ?: "VENUE"
                val vendorType = try {
                    VendorType.valueOf(vendorTypeStr.uppercase())
                } catch (e: Exception) {
                    VendorType.VENUE
                }

                val capacityRaw = vendorDoc.get("capacity")
                val capacity = when {
                    capacityRaw is Long -> capacityRaw.toInt()
                    capacityRaw is String -> capacityRaw.toIntOrNull() ?: (if (vendorType == VendorType.VENUE) DEFAULT_CAPACITY_VENUE else DEFAULT_CAPACITY_DECORATOR)
                    else -> {
                        android.util.Log.w(TAG, "Missing or invalid capacity for vendor ${booking.vendorId}. Using fallbacks: Venue=1, Decorator=4.")
                        if (vendorType == VendorType.VENUE) DEFAULT_CAPACITY_VENUE else DEFAULT_CAPACITY_DECORATOR
                    }
                }

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
                    if (overlap) throw Exception("CONFLICT_SLOT_OCCUPIED")
                } else {
                    if (currentJobCount >= capacity) {
                        throw Exception("CONFLICT_CAPACITY_FULL")
                    }
                }

                // 5. Commit Writes
                val bookingData = mutableMapOf(
                    FirestoreFields.VENDOR_ID to booking.vendorId,
                    "customerName" to booking.customerName,
                    FirestoreFields.DATE to booking.eventDate,
                    "totalAmount" to booking.totalAmount,
                    FirestoreFields.STATUS to booking.status.name,
                    FirestoreFields.SLOT_TYPE to booking.slotType.name,
                    "vendorType" to vendorType.name,
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
            // Hardened approach: Fetch occupancy AND vendor capacity/type for authoritative check
            val vendorRef = firestore.collection(FirestorePaths.COL_VENDORS).document(vendorId)
            val occupancyRef = firestore.collection(FirestorePaths.COL_OCCUPANCY).document("${vendorId}_${date}")
            val blockRef = firestore.collection(FirestorePaths.COL_BLOCKED_DATES).document("${vendorId}_${date}")

            val vendorDoc = vendorRef.get().await()
            if (!vendorDoc.exists()) return Result.failure(Exception("ERROR_VENDOR_NOT_FOUND"))

            val blockDoc = blockRef.get().await()
            if (blockDoc.exists()) return Result.success(true)

            val occupancyDoc = occupancyRef.get().await()
            if (!occupancyDoc.exists()) return Result.success(false)

            // Dynamic logic alignment
            val vendorTypeStr = vendorDoc.getString("vendorType") ?: vendorDoc.getString("primaryServiceCategory") ?: "VENUE"
            val vendorType = try {
                VendorType.valueOf(vendorTypeStr.uppercase())
            } catch (e: Exception) {
                VendorType.VENUE
            }

            val capacityRaw = vendorDoc.get("capacity")
            val capacity = when {
                capacityRaw is Long -> capacityRaw.toInt()
                capacityRaw is String -> capacityRaw.toIntOrNull() ?: (if (vendorType == VendorType.VENUE) DEFAULT_CAPACITY_VENUE else DEFAULT_CAPACITY_DECORATOR)
                else -> if (vendorType == VendorType.VENUE) DEFAULT_CAPACITY_VENUE else DEFAULT_CAPACITY_DECORATOR
            }

            val currentSlots = (occupancyDoc.get(FirestoreFields.SLOTS_OCCUPIED) as? List<*>)?.map { it.toString() } ?: emptyList()
            val currentJobCount = occupancyDoc.getLong(FirestoreFields.JOB_COUNT) ?: 0L

            val hasConflict = if (vendorType == VendorType.VENUE) {
                when (slotType) {
                    SlotType.FULL_DAY -> currentSlots.isNotEmpty()
                    SlotType.MORNING -> currentSlots.contains(SlotType.FULL_DAY.name) || currentSlots.contains(SlotType.MORNING.name)
                    SlotType.EVENING -> currentSlots.contains(SlotType.FULL_DAY.name) || currentSlots.contains(SlotType.EVENING.name)
                }
            } else {
                currentJobCount >= capacity
            }
            Result.success(hasConflict)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
