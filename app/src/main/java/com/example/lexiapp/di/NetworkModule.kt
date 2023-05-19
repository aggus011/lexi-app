package com.example.lexiapp.di

import com.example.lexiapp.data.api.LetterRepositoryImpl
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationClient
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

//Configure dependency injection with Koin here
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_WORD_URL="https://random-word-api.herokuapp.com/"

    @Singleton
    @Provides
    @Named("word_retrofit")
    fun provideRetrofit(): Retrofit {
         return Retrofit.Builder()
            .baseUrl(BASE_WORD_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideWordApiClient(@Named("word_retrofit")retrofit: Retrofit): WordAssociationClient {
        return retrofit.create(WordAssociationClient::class.java)
    }

}


