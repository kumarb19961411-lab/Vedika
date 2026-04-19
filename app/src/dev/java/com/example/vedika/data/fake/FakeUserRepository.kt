package com.example.vedika.data.fake

import com.example.vedika.core.data.model.AppUser
import com.example.vedika.core.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeUserRepository @Inject constructor() : UserRepository {
    
    private val _mockUserState = MutableStateFlow<AppUser?>(null)

    override suspend fun getUserProfile(uid: String): Result<AppUser> {
        val mock = _mockUserState.value
        if (mock != null && mock.uid == uid) {
            return Result.success(mock)
        }
        return Result.failure(Exception("USER_NOT_FOUND"))
    }

    override suspend fun createUserProfile(user: AppUser): Result<Unit> {
        _mockUserState.value = user
        return Result.success(Unit)
    }

    override fun getUserProfileStream(uid: String): Flow<AppUser?> {
        return _mockUserState.asStateFlow()
    }
}
