package com.example.lexiapp.data.api.openai_audio

import com.example.lexiapp.data.api.openai_audio.model.Texts
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SpeechToTextClient {

    @Headers(
        "Authorization: Bearer OPEN_AI_API_KEY",
    )
    @Multipart
    @POST("audio/transcriptions")
    suspend fun transcription(
        @Part file: MultipartBody.Part,
        @Part("model") model: RequestBody,
        @Part("language") language: RequestBody
    ): Response<Texts>
}