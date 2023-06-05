package com.example.lexiapp.di

import com.example.lexiapp.data.network.FirestoreClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Singleton
    @Provides
    fun provideFirestoreClient(): FirestoreClient {
        return FirestoreClient
    }

}