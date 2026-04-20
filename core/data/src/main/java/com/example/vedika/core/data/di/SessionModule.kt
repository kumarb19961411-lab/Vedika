package com.example.vedika.core.data.di

import com.example.vedika.core.data.session.EncryptedSessionStorageImpl
import com.example.vedika.core.data.session.SessionStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {

    @Binds
    @Singleton
    abstract fun bindSessionStorage(
        impl: EncryptedSessionStorageImpl
    ): SessionStorage
}
