package com.example.lexiapp.data.api.word_asociation_api

import com.example.lexiapp.data.api.word_asociation_api.model.Rows
import com.example.lexiapp.data.api.word_asociation_api.model.SendInformation
import com.example.lexiapp.data.api.word_asociation_api.model.Texts
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpeechToTextService @Inject constructor() {

    private val serviceGetTextFromPrompt: SpeechToTextClient = Retrofit.Builder()
        .baseUrl("https://api.openai.com/v1/audio/")
        .client(
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SpeechToTextClient::class.java)


    private val serviceGetDifferenceFromPrompt: SpeechToTextClient = Retrofit.Builder()
        .baseUrl("https://api.diffchecker.com/public/")
        .client(
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SpeechToTextClient::class.java)


    suspend fun transcription(path: MultipartBody.Part): Response<Texts> {
        val model = "whisper-1".toRequestBody("text/plain".toMediaTypeOrNull())
        val language = "es".toRequestBody("text/plain".toMediaTypeOrNull())

        return serviceGetTextFromPrompt.transcription(
            model = model,
            file = path,
            language = language
        )
    }

    suspend fun getDifference(sendInformation: SendInformation): Response<Rows> =
        serviceGetDifferenceFromPrompt.getDifference(SendInformation = sendInformation)
}
