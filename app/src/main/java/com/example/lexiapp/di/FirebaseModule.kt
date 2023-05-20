package com.example.lexiapp.di

import com.example.lexiapp.domain.useCases.LoginUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {


    @Provides
    fun provideLoginUseCases() = LoginUseCases()
}