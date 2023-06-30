package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsGateway
import com.example.lexiapp.domain.service.OpenAICompletionsService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenAICompletionsServiceImpl @Inject constructor(
    private val openAICompletionsService: OpenAICompletionsGateway
): OpenAICompletionsService {
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