package com.example.lexiapp.data.api.openaicompletions

import com.example.lexiapp.data.api.openaicompletions.model.OpenAICompletionsRequest
import com.example.lexiapp.data.api.openaicompletions.model.OpenAICompletionsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAICompletionsClient {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer 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"
    )
    @POST("completions")
    suspend fun createChallengeReading(@Body chatGptChatRequest: OpenAICompletionsRequest): Response<OpenAICompletionsResponse>

}