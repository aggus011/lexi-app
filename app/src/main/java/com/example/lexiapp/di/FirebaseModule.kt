package com.example.lexiapp.di

import com.example.lexiapp.domain.useCases.LoginUseCases
import com.google.firebase.auth.FirebaseAuth
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
}