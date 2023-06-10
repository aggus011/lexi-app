package com.example.lexiapp.di

import android.content.SharedPreferences
import com.example.lexiapp.data.api.LetterRepositoryImpl
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsRepositoryImpl
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsService
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import com.example.lexiapp.data.network.FireStoreService
import com.example.lexiapp.data.repository.challengereading.ChallengeReadingRepositoryImpl
import com.example.lexiapp.domain.service.ChallengeReadingRepository
import com.example.lexiapp.domain.service.LetterRepository
import com.example.lexiapp.domain.service.OpenAICompletionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
object InterfaceModule {

    @Provides
    fun getLetterRepository(
        apiWordService: WordAssociationService,
        db: FireStoreService,
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
        firestoreService: FireStoreService
    ): ChallengeReadingRepository {
        return ChallengeReadingRepositoryImpl(firestoreService)
    }
}