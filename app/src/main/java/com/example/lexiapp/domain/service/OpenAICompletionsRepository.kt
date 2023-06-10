package com.example.lexiapp.domain.service

import kotlinx.coroutines.flow.Flow

interface OpenAICompletionsRepository {

    suspend fun getChallengeReading(prompt: String, challengeWords: List<String>): Flow<String>

}
