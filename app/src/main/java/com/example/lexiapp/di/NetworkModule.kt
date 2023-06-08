package com.example.lexiapp.di

import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsClient
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationClient
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
    private const val BASE_CHATGPT_CHAT_URL="https://api.openai.com/v1/"

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

    @Singleton
    @Provides
    @Named("challenge_reading_retrofit")
    fun provideChatGptChatRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_CHATGPT_CHAT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideChatGptChatClient(@Named("challenge_reading_retrofit")retrofit: Retrofit): OpenAICompletionsClient {
        return retrofit.create(OpenAICompletionsClient::class.java)
    }
}


