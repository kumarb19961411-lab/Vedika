package com.example.vedika.core.data.repository.firebase

/**
 * Centralized Firestore constants to maintain schema consistency across the app.
 */
object FirestorePaths {
    const val COL_VENDORS = "vendors"
    const val COL_BOOKINGS = "bookings"
    const val COL_BLOCKED_DATES = "blocked_dates"
    const val COL_INVENTORY = "inventory"
    
    // Sub-collections or logical groupings
    const val COL_RESERVATIONS = "reservations"
    const val COL_OCCUPANCY = "occupancy"
}

object FirestoreFields {
    const val VENDOR_ID = "vendorId"
    const val DATE = "eventDate"
    const val STATUS = "status"
    const val SLOT_TYPE = "slotType"
    const val CREATED_AT = "createdAt"
    const val JOB_COUNT = "jobCount"
    const val SLOTS_OCCUPIED = "slotsOccupied"
}
