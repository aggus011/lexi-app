package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.gameResult.ResultGame
import kotlinx.coroutines.flow.Flow

interface LetterService {

    suspend fun getWord(count: Int, length: Int, language: String): Flow<String>

    suspend fun saveResult(result: ResultGame)

    suspend fun generateNotificationIfObjectiveHasBeenCompleted(game: String, typeGame: String, gameName: String)

}