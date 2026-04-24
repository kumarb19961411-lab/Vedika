package com.example.vedika.core.data.di

import com.example.vedika.core.data.util.AndroidVedikaLogger
import com.example.vedika.core.data.util.VedikaLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LoggerModule {

    @Binds
    @Singleton
    abstract fun bindLogger(logger: AndroidVedikaLogger): VedikaLogger
}
