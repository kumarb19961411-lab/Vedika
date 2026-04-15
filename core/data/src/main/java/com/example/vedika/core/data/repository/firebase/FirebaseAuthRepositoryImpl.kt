package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val vendorRepository: VendorRepository
) : AuthRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getActiveVendor(): Flow<VendorUser?> = callbackFlow<String?> {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser?.uid)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }.flatMapLatest { uid ->
        if (uid != null) {
            vendorRepository.getVendorProfileStream(uid)
        } else {
            kotlinx.coroutines.flow.flowOf(null)
        }
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
