package com.example.lexiapp.di

import android.content.SharedPreferences
import com.example.lexiapp.data.network.TextScannerServiceImpl
import com.example.lexiapp.data.api.difference_text.DifferenceGateway
import com.example.lexiapp.data.api.notifications.FirebaseNotificationGateway
import com.example.lexiapp.data.api.openai_audio.SpeechToTextGateway
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsGateway
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationGateway
import com.example.lexiapp.data.network.*
import com.example.lexiapp.data.repository.texttoread.TextToReadRepositoryImpl
import com.example.lexiapp.domain.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object InterfaceModule {

    @Provides
    fun getLetterRepository(
        apiWordService: WordAssociationGateway,
        db: FireStoreServiceImpl,
        prefs: SharedPreferences,
        notifications: FirebaseNotificationServiceImpl
    ): LetterService {
        return LetterServiceImpl(apiWordService, db, prefs, notifications)
    }

    @Provides
    fun getOpenAICompletionsRepository(
        openAICompletionsService: OpenAICompletionsGateway
    ): OpenAICompletionsService {
        return OpenAICompletionsServiceImpl(openAICompletionsService)
    }

    @Provides
    fun getAmdinService(
        db: FireStoreServiceImpl
    ): AdminService {
        return AdminServiceImpl(db)
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
        db: FireStoreService,
        notifications: FirebaseNotificationServiceImpl
    ): DifferenceService {
        return DifferenceServiceImpl(apiDifferenceGateway, db, notifications)
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

    @Provides
    fun getFirebaseNotificationService(
        firebaseNotificationGateway: FirebaseNotificationGateway
    ): FirebaseNotificationService {
        return FirebaseNotificationServiceImpl(firebaseNotificationGateway)
    }

    @Provides
    fun getTextScannerService(
        db: FireStoreServiceImpl,
        notifications: FirebaseNotificationServiceImpl
    ): TextScannerService {
        return TextScannerServiceImpl(db, notifications)
    }

    @Provides
    fun getTextToReadRepository(): TextToReadRepository {
        return TextToReadRepositoryImpl()
    }
}