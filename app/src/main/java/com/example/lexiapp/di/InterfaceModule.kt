package com.example.lexiapp.di

import android.content.SharedPreferences
import com.example.lexiapp.data.api.LetterRepositoryImpl
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import com.example.lexiapp.data.network.FireStoreService
import com.example.lexiapp.domain.service.LetterRepository
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
}