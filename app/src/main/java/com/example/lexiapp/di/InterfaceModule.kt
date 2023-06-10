package com.example.lexiapp.di

import android.content.SharedPreferences
import com.example.lexiapp.data.api.LetterRepositoryImpl
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsRepositoryImpl
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsService
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import com.example.lexiapp.data.network.AuthenticationServiceImpl
import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.data.network.FirebaseClient
import com.example.lexiapp.data.repository.challengereading.ChallengeReadingRepositoryImpl
import com.example.lexiapp.domain.service.AuthenticationService
import com.example.lexiapp.domain.service.ChallengeReadingRepository
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.LetterRepository
import com.example.lexiapp.domain.service.OpenAICompletionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object InterfaceModule {

    @Provides
    fun getLetterRepository(
        apiWordService: WordAssociationService,
        db: FireStoreServiceImpl,
        prefs: SharedPreferences
    ): LetterRepository {
        return LetterRepositoryImpl(apiWordService, db, prefs)
    }

    @Provides
    fun getOpenAICompletionsRepository(
        openAICompletionsService: OpenAICompletionsService
    ): OpenAICompletionsRepository {
        return OpenAICompletionsRepositoryImpl(openAICompletionsService)
    }

    @Provides
    fun getChallengeReadingRepository(
        firestoreService: FireStoreServiceImpl
    ): ChallengeReadingRepository {
        return ChallengeReadingRepositoryImpl(firestoreService)
    }

    @Provides
    fun getAuthenticationService(
        firebase: FirebaseClient
    ): AuthenticationService {
        return AuthenticationServiceImpl(firebase)
    }

    @Provides
    fun getFirestoreService(
        firebase: FirebaseClient
    ): FireStoreService {
        return FireStoreServiceImpl(firebase)
    }
}