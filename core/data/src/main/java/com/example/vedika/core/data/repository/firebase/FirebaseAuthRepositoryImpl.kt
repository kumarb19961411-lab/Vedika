package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AuthRepository {

    override fun getActiveVendor(): Flow<VendorUser?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // Return a basic shell, but Vendor properties must come from Firestore.
                // For Phase 2 we assume the vendor is derived in combination.
                // Since this method just returns the state, we pass basic info.
                trySend(
                    VendorUser(
                        id = user.uid,
                        ownerName = user.displayName ?: "Live Vendor",
                        businessName = "Configuring...",
                        primaryServiceCategory = "",
                        isVerified = false
                    )
                )
            } else {
                trySend(null)
            }
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun sendOtp(phoneNumber: String): Result<String> {
        // OTP logic deferred to UI callback binding in Prod auth.
        // Returning failure here unless PhoneAuthProvider handles it fully.
        return Result.failure(Exception("Live OTP via Repo suspend is unimplemented. Use PhoneAuthProvider in Activity."))
    }

    override suspend fun verifyOtp(verificationId: String, otp: String): Result<VendorUser> {
        // Deferred until complete Activity-level PhoneAuthProvider is established.
        return Result.failure(Exception("Live OTP Verify is unimplemented"))
    }

    override suspend fun loginAsDevBypass(username: String): Result<VendorUser> {
        return Result.failure(Exception("No dev bypass allowed on Live Firebase target. Use dev flavor."))
    }

    override suspend fun logout() {
        auth.signOut()
    }
}
