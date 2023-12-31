package com.example.lexiapp.di

import com.example.lexiapp.data.api.difference_text.DifferenceClient
import com.example.lexiapp.data.api.openai_audio.SpeechToTextClient
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsClient
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationClient
import com.example.lexiapp.data.api.notifications.FirebaseNotificationClient
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
    private const val NEW_WORD_API_URL="https://api.wordassociations.net/associations/v1.0/json/"
    private const val BASE_CHATGPT_CHAT_URL="https://api.openai.com/v1/"
    private const val BASE_DIFFERENCE_URL="https://api.diffchecker.com/public/"
    private const val BASE_FIREBASE_SEND_NOTIFICATIONS_URL="https://fcm.googleapis.com/"

    @Singleton
    @Provides
    @Named("word_retrofit")
    fun provideRetrofit(): Retrofit {
         return Retrofit.Builder()
            .baseUrl(NEW_WORD_API_URL)
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

    @Singleton
    @Provides
    fun provideOpenAiTranscriptionClient(@Named("challenge_reading_retrofit")retrofit: Retrofit): SpeechToTextClient =
        retrofit.create(SpeechToTextClient::class.java)

    @Singleton
    @Provides
    @Named("difference_retrofit")
    fun provideDifferenceRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_DIFFERENCE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideDifferenceClient(@Named("difference_retrofit")retrofit: Retrofit) : DifferenceClient =
        retrofit.create(DifferenceClient::class.java)

    @Singleton
    @Provides
    @Named("firebase_notification_retrofit")
    fun provideNotificationRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_FIREBASE_SEND_NOTIFICATIONS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun provideNotificationClient(@Named("firebase_notification_retrofit")retrofit: Retrofit): FirebaseNotificationClient =
        retrofit.create(FirebaseNotificationClient::class.java)
}


