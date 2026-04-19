package com.example.vedika.core.data.repository.firebase

import com.example.vedika.core.data.model.AppUser
import com.example.vedika.core.data.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseUserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun getUserProfile(uid: String): Result<AppUser> {
        return try {
            val docInfo = usersCollection.document(uid).get().await()
            if (docInfo.exists()) {
                val user = AppUser(
                    uid = docInfo.id,
                    fullName = docInfo.getString("fullName") ?: "",
                    phoneNumber = docInfo.getString("phoneNumber") ?: "",
                    createdAt = docInfo.getLong("createdAt") ?: 0L,
                    userType = docInfo.getString("userType") ?: "USER",
                    profileImageUrl = docInfo.getString("profileImageUrl")
                )
                Result.success(user)
            } else {
                Result.failure(Exception("USER_NOT_FOUND"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createUserProfile(user: AppUser): Result<Unit> {
        return try {
            val data = hashMapOf(
                "uid" to user.uid,
                "fullName" to user.fullName,
                "phoneNumber" to user.phoneNumber,
                "createdAt" to user.createdAt,
                "userType" to user.userType,
                "profileImageUrl" to user.profileImageUrl
            )
            usersCollection.document(user.uid).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUserProfileStream(uid: String): Flow<AppUser?> = callbackFlow {
        val listener = usersCollection.document(uid).addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(null)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val user = AppUser(
                    uid = snapshot.id,
                    fullName = snapshot.getString("fullName") ?: "",
                    phoneNumber = snapshot.getString("phoneNumber") ?: "",
                    createdAt = snapshot.getLong("createdAt") ?: 0L,
                    userType = snapshot.getString("userType") ?: "USER",
                    profileImageUrl = snapshot.getString("profileImageUrl")
                )
                trySend(user)
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }
}
