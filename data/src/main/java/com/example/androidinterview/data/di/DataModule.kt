package com.example.androidinterview.data.di

import com.example.androidinterview.data.repository.MerchantRepositoryImpl
import com.example.androidinterview.domain.repository.MerchantRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun bindMerchantRepository(impl: MerchantRepositoryImpl): MerchantRepository

    companion object {
        @Provides
        @Singleton
        @IoDispatcher
        fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO
    }
}
