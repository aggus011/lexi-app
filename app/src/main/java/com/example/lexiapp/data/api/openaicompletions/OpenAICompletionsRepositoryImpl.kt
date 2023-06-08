package com.example.lexiapp.data.api.openaicompletions

import android.util.Log
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenAICompletionsRepositoryImpl @Inject constructor(
    private val openAICompletionsService: OpenAICompletionsService
) {
    suspend fun getChallengeReading(prompt: String, challengeWords: List<String>) = flow {
        openAICompletionsService.getChallengeReading(prompt, challengeWords)
            .collect{ challengeReading ->
                emit(challengeReading)
                Log.v(TAG, challengeReading)
            }
    }

    companion object{
        const val TAG = "OpenAICompletionsRepository"
    }
}