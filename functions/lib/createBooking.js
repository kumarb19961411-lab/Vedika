"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.createBooking = void 0;
const functions = require("firebase-functions");
const admin = require("firebase-admin");
exports.createBooking = functions.https.onCall(async (data, context) => {
    var _a;
    // 1. Authenticate user
    if (!context.auth) {
        throw new functions.https.HttpsError('unauthenticated', 'The function must be called while authenticated.');
    }
    const uid = context.auth.uid;
    // 2. Extract and validate input
    const { vendorId, eventDate, eventType, guestCount, slotType, notes, idempotencyKey } = data;
    if (!vendorId || !eventDate || !eventType || !slotType || !idempotencyKey) {
        throw new functions.https.HttpsError('invalid-argument', 'Missing required parameters: vendorId, eventDate, eventType, slotType, or idempotencyKey.');
    }
    const db = admin.firestore();
    // 3. Fetch user data (customerName)
    const userRef = db.collection('users').doc(uid);
    const userDoc = await userRef.get();
    const customerName = userDoc.exists ? ((_a = userDoc.data()) === null || _a === void 0 ? void 0 : _a.name) || 'Unknown' : 'Unknown';
    const dateId = `${vendorId}_${eventDate}`;
    const blockRef = db.collection('blocked_dates').doc(dateId);
    const vendorRef = db.collection('vendors').doc(vendorId);
    const occupancyRef = db.collection('occupancy').doc(dateId);
    // Use idempotency collection to prevent duplicate requests
    const idempotencyRef = db.collection('idempotency_keys').doc(`${uid}_${idempotencyKey}`);
    try {
        return await db.runTransaction(async (transaction) => {
            // Check Idempotency
            const idempotencyDoc = await transaction.get(idempotencyRef);
            if (idempotencyDoc.exists) {
                throw new functions.https.HttpsError('already-exists', 'A booking request with this idempotency key has already been processed.');
            }
            // Check blocked dates
            const blockDoc = await transaction.get(blockRef);
            if (blockDoc.exists) {
                throw new functions.https.HttpsError('failed-precondition', 'CONFLICT_DATE_BLOCKED');
            }
            // Fetch Vendor Profile
            const vendorDoc = await transaction.get(vendorRef);
            if (!vendorDoc.exists) {
                throw new functions.https.HttpsError('not-found', 'ERROR_VENDOR_NOT_FOUND');
            }
            const vendorData = vendorDoc.data() || {};
            const vendorTypeStr = vendorData.vendorType || vendorData.primaryServiceCategory || 'VENUE';
            const vendorType = vendorTypeStr.toUpperCase();
            let capacity = 1; // Default
            if (vendorData.capacity) {
                capacity = typeof vendorData.capacity === 'number' ? vendorData.capacity : parseInt(vendorData.capacity, 10);
            }
            else {
                capacity = vendorType === 'VENUE' ? 1 : 4;
            }
            // Check Occupancy
            const occupancyDoc = await transaction.get(occupancyRef);
            const occupancyData = occupancyDoc.data();
            const currentSlots = (occupancyData === null || occupancyData === void 0 ? void 0 : occupancyData.slotsOccupied) || [];
            const currentJobCount = (occupancyData === null || occupancyData === void 0 ? void 0 : occupancyData.jobCount) || 0;
            // Apply Conflict Logic
            if (vendorType === 'VENUE') {
                let overlap = false;
                if (slotType === 'FULL_DAY') {
                    overlap = currentSlots.length > 0;
                }
                else if (slotType === 'MORNING') {
                    overlap = currentSlots.includes('FULL_DAY') || currentSlots.includes('MORNING');
                }
                else if (slotType === 'EVENING') {
                    overlap = currentSlots.includes('FULL_DAY') || currentSlots.includes('EVENING');
                }
                if (overlap) {
                    throw new functions.https.HttpsError('failed-precondition', 'CONFLICT_SLOT_OCCUPIED');
                }
            }
            else {
                if (currentJobCount >= capacity) {
                    throw new functions.https.HttpsError('failed-precondition', 'CONFLICT_CAPACITY_FULL');
                }
            }
            // Commit Writes
            const totalAmount = vendorData.price || 0; // Derive from vendor data instead of client
            const bookingData = {
                userId: uid,
                vendorId: vendorId,
                customerName: customerName,
                eventDate: eventDate,
                eventType: eventType,
                guestCount: guestCount || 0,
                notes: notes || "",
                totalAmount: totalAmount,
                status: "PENDING",
                slotType: slotType,
                vendorType: vendorType,
                createdAt: admin.firestore.FieldValue.serverTimestamp()
            };
            const bookingRef = db.collection('bookings').doc();
            transaction.set(bookingRef, bookingData);
            // Update Occupancy
            if (occupancyDoc.exists) {
                transaction.update(occupancyRef, {
                    slotsOccupied: admin.firestore.FieldValue.arrayUnion(slotType),
                    jobCount: admin.firestore.FieldValue.increment(1)
                });
            }
            else {
                transaction.set(occupancyRef, {
                    vendorId: vendorId,
                    date: eventDate,
                    slotsOccupied: [slotType],
                    jobCount: 1
                });
            }
            // Record Idempotency Key
            transaction.set(idempotencyRef, {
                bookingId: bookingRef.id,
                createdAt: admin.firestore.FieldValue.serverTimestamp()
            });
            return { bookingId: bookingRef.id };
        });
    }
    catch (error) {
        if (error instanceof functions.https.HttpsError) {
            throw error;
        }
        throw new functions.https.HttpsError('internal', error.message || 'An unexpected error occurred.');
    }
});
//# sourceMappingURL=createBooking.js.map