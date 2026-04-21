package com.example.vedika.core.data.repository.firebase

import android.util.Log
import com.example.vedika.core.data.model.VendorUser
import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

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
            vendorRepository.getVendorProfileStream(uid).map { profile ->
                profile?.let {
                    VendorUser(
                        id = it.id,
                        businessName = it.businessName,
                        ownerName = it.ownerName,
                        isVerified = it.isVerified,
                        primaryServiceCategory = it.primaryCategory
                    )
                }
            }
        } else {
            kotlinx.coroutines.flow.flowOf(null)
        }
    }


    override suspend fun sendOtp(phoneNumber: String, activity: Activity): Result<String> = suspendCancellableCoroutine { continuation ->
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d("VedikaDebug", "AuthRepo: onVerificationCompleted (Auto-retrieval or Instant Validation)")
                    // Auto-retrieval handled by implicit device services if supported. 
                    // To keep state deterministic, we rely on the OTP input step for now.
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("VedikaDebug", "AuthRepo: onVerificationFailed - ${e.message}", e)
                    if (continuation.isActive) continuation.resume(Result.failure(e))
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    Log.d("VedikaDebug", "AuthRepo: onCodeSent - VerificationId: $verificationId")
                    if (continuation.isActive) continuation.resume(Result.success(verificationId))
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun verifyOtp(verificationId: String, otp: String): Result<VendorUser> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, otp)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user 
            if (user != null) {
                // Return placeholder user populated ONLY with uid. Subsequent layers will fetch true profile.
                Result.success(VendorUser(
                    id = user.uid,
                    businessName = "",
                    ownerName = "",
                    isVerified = false,
                    primaryServiceCategory = ""
                ))
            } else {
                Result.failure(Exception("Sign in succeeded but returned null user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginAsDevBypass(username: String): Result<VendorUser> {
        return Result.failure(Exception("No dev bypass allowed on Live Firebase target. Use dev flavor."))
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
