package com.example.lexiapp.data.api.word_asociation_api

import com.example.lexiapp.data.api.word_asociation_api.model.Texts
import com.example.lexiapp.data.api.word_asociation_api.model.TranscriptionInformation
import com.example.lexiapp.utils.OPEN_AI_API_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SpeechToTextClient {

    @Headers("Content-Type: application/json",
        "Authorization: Bearer $OPEN_AI_API_KEY")
    @POST("transcriptions")
    suspend fun transcription(@Body TranscriptionInformation: TranscriptionInformation) : Response<Texts>
}