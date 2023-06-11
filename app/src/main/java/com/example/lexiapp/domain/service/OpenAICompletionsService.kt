package com.example.lexiapp.domain.service

import kotlinx.coroutines.flow.Flow

interface OpenAICompletionsService {

    suspend fun getChallengeReading(prompt: String, challengeWords: List<String>): Flow<String>

}
