package com.example.lexiapp.data.api.word_asociation_api

import com.example.lexiapp.data.api.word_asociation_api.model.Rows
import com.example.lexiapp.data.api.word_asociation_api.model.SendInformation
import com.example.lexiapp.data.api.word_asociation_api.model.Texts
import com.example.lexiapp.utils.OPEN_AI_API_KEY
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface SpeechToTextClient {

    @Headers(
        "Authorization: Bearer $OPEN_AI_API_KEY",
    )
    @Multipart
    @POST("transcriptions")
    suspend fun transcription(
        @Part file: MultipartBody.Part,
        @Part("model") model: RequestBody,
        @Part("language") language: RequestBody
    ): Response<Texts>

    @POST("text?output_type=json&email=lexiapp.2023@gmail.com")
    suspend fun getDifference(@Header("Content-Type") contentType: String = "application/json",
                              @Body SendInformation: SendInformation): Response<Rows>
}