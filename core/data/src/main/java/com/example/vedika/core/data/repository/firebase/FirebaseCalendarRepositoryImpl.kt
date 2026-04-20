package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.*
import com.example.vedika.core.data.repository.CalendarRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.repository.BookingRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FirebaseCalendarRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val vendorRepository: VendorRepository,
    private val bookingRepository: BookingRepository
) : CalendarRepository {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun getCalendarState(vendorId: String, month: Int, year: Int): Flow<Map<LocalDate, CalendarDayState>> {
        val startOfMonth = LocalDate.of(year, month, 1)
        val endOfMonth = startOfMonth.plusMonths(1).minusDays(1)
        
        val startMillis = startOfMonth.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val endMillis = endOfMonth.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        // 1. Listen to Bookings for this range
        val bookingsFlow = bookingRepository.getBookingsForVendor(vendorId).map { list ->
            list.filter { it.eventDate in startMillis..endMillis }
        }

        // 2. Listen to Blocked Dates for this range
        val blockedFlow = callbackFlow<List<BlockedDate>> {
            val subscription = firestore.collection(FirestorePaths.COL_BLOCKED_DATES)
                .whereEqualTo(FirestoreFields.VENDOR_ID, vendorId)
                // Note: Index required for range query on date if using Timestamp
                // For scaffolding, we use a simpler pattern if index is missing
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    val blocks = snapshot?.documents?.mapNotNull { doc ->
                        try {
                            BlockedDate(
                                vendorId = doc.getString(FirestoreFields.VENDOR_ID) ?: "",
                                date = doc.getLong(FirestoreFields.DATE) ?: 0L,
                                reason = doc.getString("reason") ?: "",
                                slotType = SlotType.valueOf(doc.getString(FirestoreFields.SLOT_TYPE) ?: "FULL_DAY")
                            )
                        } catch (e: Exception) { null }
                    } ?: emptyList()
                    trySend(blocks.filter { it.date in startMillis..endMillis })
                }
            awaitClose { subscription.remove() }
        }

        // 3. Combine with Vendor Type to derive state
        return combine(bookingsFlow, blockedFlow, flow { emit(vendorRepository.getVendorProfile(vendorId).getOrNull()) }) { bookings, blocks, vendor ->
            val calendarMap = mutableMapOf<LocalDate, CalendarDayState>()
            
            val vendorTypeStr = vendor?.vendorType ?: vendor?.primaryServiceCategory ?: "VENUE"
            val vendorType = try {
                VendorType.valueOf(vendorTypeStr.uppercase())
            } catch (e: Exception) {
                if (vendorTypeStr.contains("Venue", ignoreCase = true)) VendorType.VENUE else VendorType.DECORATOR
            }

            val capacityRaw = vendor?.capacity
            val capacity = when {
                capacityRaw is Int -> capacityRaw
                capacityRaw is String -> capacityRaw.toIntOrNull() ?: (if (vendorType == VendorType.VENUE) 1 else 4)
                else -> if (vendorType == VendorType.VENUE) 1 else 4
            }
            
            // Populate range
            var current = startOfMonth
            while (!current.isAfter(endOfMonth)) {
                val dateMillis = current.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
                val dayBookings = bookings.filter { it.eventDate == dateMillis }
                val dayBlocks = blocks.filter { it.date == dateMillis }
                
                calendarMap[current] = deriveDayState(current, vendorType, capacity, dayBookings, dayBlocks)
                current = current.plusDays(1)
            }
            calendarMap
        }
    }

    private fun deriveDayState(
        date: LocalDate,
        vendorType: VendorType,
        capacity: Int,
        bookings: List<Booking>,
        blocks: List<BlockedDate>
    ): CalendarDayState {
        val isBlocked = blocks.any { it.slotType == SlotType.FULL_DAY }
        
        // Simple logic for scaffolding (matching FakeCalendarRepository patterns)
        val status = when {
            isBlocked -> DayAvailabilityStatus.BLOCKED
            vendorType == VendorType.VENUE -> {
                val hasFullDay = bookings.any { it.slotType == SlotType.FULL_DAY && it.status == BookingStatus.CONFIRMED }
                val morning = bookings.any { it.slotType == SlotType.MORNING && it.status == BookingStatus.CONFIRMED }
                val evening = bookings.any { it.slotType == SlotType.EVENING && it.status == BookingStatus.CONFIRMED }
                
                when {
                    hasFullDay || (morning && evening) -> DayAvailabilityStatus.BOOKED
                    morning || evening || bookings.isNotEmpty() -> DayAvailabilityStatus.PENDING
                    else -> DayAvailabilityStatus.AVAILABLE
                }
            }
            vendorType == VendorType.DECORATOR -> {
                val totalBusy = bookings.filter { it.status != BookingStatus.CANCELLED }.size
                when {
                    totalBusy >= capacity -> DayAvailabilityStatus.BOOKED
                    totalBusy > 0 -> DayAvailabilityStatus.LIMITED
                    else -> DayAvailabilityStatus.AVAILABLE
                }
            }
            else -> DayAvailabilityStatus.AVAILABLE
        }

        return CalendarDayState(
            date = date,
            status = status,
            bookings = bookings,
            manualBlocks = blocks.map { it.reason }
        )
    }

    override suspend fun blockDate(vendorId: String, date: LocalDate, reason: String): Result<Unit> {
        return try {
            val dateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val docId = "${vendorId}_${dateMillis}"
            
            firestore.runTransaction { transaction ->
                val occupancyRef = firestore.collection(FirestorePaths.COL_OCCUPANCY).document(docId)
                val blockRef = firestore.collection(FirestorePaths.COL_BLOCKED_DATES).document(docId)
                
                // 1. Check if ANY bookings exist for this date
                val occupancyDoc = transaction.get(occupancyRef)
                if (occupancyDoc.exists()) {
                    val currentSlots = (occupancyDoc.get(FirestoreFields.SLOTS_OCCUPIED) as? List<*>) ?: emptyList<Any>()
                    val jobCount = occupancyDoc.getLong(FirestoreFields.JOB_COUNT) ?: 0L
                    
                    if (currentSlots.isNotEmpty() || jobCount > 0) {
                        throw Exception("ACTIVE_BOOKINGS_EXIST: Cannot block a date that already has active bookings.")
                    }
                }
                
                // 2. Write the block
                val data = mapOf(
                    FirestoreFields.VENDOR_ID to vendorId,
                    FirestoreFields.DATE to dateMillis,
                    "reason" to reason,
                    FirestoreFields.SLOT_TYPE to SlotType.FULL_DAY.name
                )
                transaction.set(blockRef, data)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unblockDate(vendorId: String, date: LocalDate): Result<Unit> {
        return try {
            val dateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val docId = "${vendorId}_${dateMillis}"
            firestore.collection(FirestorePaths.COL_BLOCKED_DATES).document(docId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isDateBlocked(vendorId: String, date: LocalDate): Boolean {
        val dateMillis = date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val snapshot = firestore.collection(FirestorePaths.COL_BLOCKED_DATES)
            .whereEqualTo(FirestoreFields.VENDOR_ID, vendorId)
            .whereEqualTo(FirestoreFields.DATE, dateMillis)
            .get()
            .await()
        return !snapshot.isEmpty
    }
}

// Internal helper model for scaffolding
private data class BlockedDate(
    val vendorId: String,
    val date: Long,
    val reason: String,
    val slotType: SlotType
)
