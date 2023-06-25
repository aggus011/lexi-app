package com.example.lexiapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun provideLoginUseCases() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideDB() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideNotifications() = FirebaseMessaging.getInstance()
}