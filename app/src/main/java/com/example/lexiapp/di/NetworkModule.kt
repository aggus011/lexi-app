package com.example.lexiapp.di

import com.example.lexiapp.data.api.word_asociation_api.WordAssociationClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//Configure dependency injection with Koin here
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
         return Retrofit.Builder()
            .baseUrl("https://api.wordassociations.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideWordApiClient(retrofit: Retrofit): WordAssociationClient {
        return retrofit.create(WordAssociationClient::class.java)
    }

}


