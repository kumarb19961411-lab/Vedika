package com.example.vedika.di

import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.BookingRepository
import com.example.vedika.core.data.repository.InventoryRepository
import com.example.vedika.core.data.repository.UserRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.repository.firebase.FirebaseAuthRepositoryImpl
import com.example.vedika.core.data.repository.firebase.FirebaseBookingRepositoryImpl
import com.example.vedika.core.data.repository.firebase.FirebaseInventoryRepositoryImpl
import com.example.vedika.core.data.repository.firebase.FirebaseUserRepositoryImpl
import com.example.vedika.core.data.repository.firebase.FirebaseVendorRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LiveRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: FirebaseAuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: FirebaseUserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindVendorRepository(
        impl: FirebaseVendorRepositoryImpl
    ): VendorRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        impl: FirebaseBookingRepositoryImpl
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindInventoryRepository(
        impl: FirebaseInventoryRepositoryImpl
    ): InventoryRepository
}
