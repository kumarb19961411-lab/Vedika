package com.example.vedika.di

import com.example.vedika.core.data.repository.AuthRepository
import com.example.vedika.core.data.repository.BookingRepository
import com.example.vedika.core.data.repository.InventoryRepository
import com.example.vedika.core.data.repository.UserRepository
import com.example.vedika.core.data.repository.VendorRepository
import com.example.vedika.core.data.repository.CalendarRepository
import com.example.vedika.data.fake.FakeAuthRepository
import com.example.vedika.data.fake.FakeUserRepository
import com.example.vedika.data.fake.FakeBookingRepository
import com.example.vedika.data.fake.FakeInventoryRepository
import com.example.vedika.data.fake.FakeVendorRepository
import com.example.vedika.data.fake.FakeCalendarRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DevRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        fakeAuthRepository: FakeAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        fakeUserRepository: FakeUserRepository
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindVendorRepository(
        fakeVendorRepository: FakeVendorRepository
    ): VendorRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        fakeBookingRepository: FakeBookingRepository
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindInventoryRepository(
        fakeInventoryRepository: FakeInventoryRepository
    ): InventoryRepository

    @Binds
    @Singleton
    abstract fun bindCalendarRepository(
        fakeCalendarRepository: FakeCalendarRepository
    ): CalendarRepository
}
