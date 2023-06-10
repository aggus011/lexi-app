package com.example.lexiapp.data.api.openaicompletions

import android.util.Log
import com.example.lexiapp.domain.service.OpenAICompletionsRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenAICompletionsRepositoryImpl @Inject constructor(
    private val openAICompletionsService: OpenAICompletionsService
): OpenAICompletionsRepository {
    override suspend fun getChallengeReading(prompt: String, challengeWords: List<String>) = flow {
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