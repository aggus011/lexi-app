package com.example.lexiapp.di

import android.content.SharedPreferences
import com.example.lexiapp.data.api.DifferenceServiceImpl
import com.example.lexiapp.data.api.LetterServiceImpl
import com.example.lexiapp.data.api.SpeechToTextServiceImpl
import com.example.lexiapp.data.api.difference_text.DifferenceGateway
import com.example.lexiapp.data.api.openai_audio.SpeechToTextGateway
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsGateway
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsServiceImpl
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import com.example.lexiapp.data.network.AuthenticationServiceImpl
import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.data.network.FirebaseClient
import com.example.lexiapp.data.network.ObjectivesServiceImpl
import com.example.lexiapp.data.network.ResultGamesServiceImpl
import com.example.lexiapp.data.repository.challengereading.ChallengeReadingServiceImpl
import com.example.lexiapp.domain.service.AuthenticationService
import com.example.lexiapp.domain.service.ChallengeReadingService
import com.example.lexiapp.domain.service.DifferenceService
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.LetterService
import com.example.lexiapp.domain.service.ObjectivesService
import com.example.lexiapp.domain.service.OpenAICompletionsService
import com.example.lexiapp.domain.service.ResultGamesService
import com.example.lexiapp.domain.service.SpeechToTextService
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
    ): LetterService {
        return LetterServiceImpl(apiWordService, db, prefs)
    }

    @Provides
    fun getOpenAICompletionsRepository(
        openAICompletionsService: OpenAICompletionsGateway
    ): OpenAICompletionsService {
        return OpenAICompletionsServiceImpl(openAICompletionsService)
    }

    @Provides
    fun getChallengeReadingRepository(
        firestoreService: FireStoreServiceImpl
    ): ChallengeReadingService {
        return ChallengeReadingServiceImpl(firestoreService)
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

    @Provides
    fun getSpeechToTextService(
        apiService: SpeechToTextGateway,
        firestoreService: FireStoreServiceImpl
    ): SpeechToTextService {
        return SpeechToTextServiceImpl(apiService, firestoreService)
    }

    @Provides
    fun getDifferenceService(
        apiDifferenceGateway: DifferenceGateway,
        db: FireStoreService
    ): DifferenceService {
        return DifferenceServiceImpl(apiDifferenceGateway, db)
    }

    @Provides
    fun getObjectiveService(
        firestoreService: FireStoreServiceImpl
    ): ObjectivesService {
        return ObjectivesServiceImpl(firestoreService)
    }

    @Provides
    fun getResultGameService(db: FireStoreService): ResultGamesService {
        return ResultGamesServiceImpl(db)
    }
}