package com.example.lexiapp.data.api.word_asociation_api

import android.util.Log
import com.example.lexiapp.data.api.word_asociation_api.model.Texts
import com.example.lexiapp.data.api.word_asociation_api.model.TranscriptionInformation
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpeechToTextService @Inject constructor() {
    suspend fun transcription(path: MultipartBody.Part): Response<Texts> {
        val information = TranscriptionInformation()
        val model = "whisper-1".toRequestBody("text/plain".toMediaTypeOrNull())
        val language = "es".toRequestBody("text/plain".toMediaTypeOrNull())
        return serviceGetTextFromPrompt.transcription(model = model, language = language, file = path)
    }


    private val serviceGetTextFromPrompt: SpeechToTextClient = Retrofit.Builder()
        .baseUrl("https://api.openai.com/v1/audio/")
        //.client(clientOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SpeechToTextClient::class.java)

    private fun buildService(): SpeechToTextClient {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build()
            val builder: Retrofit.Builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())

            val retrofit: Retrofit = builder.build()
            return retrofit.create(SpeechToTextClient::class.java)
        }


        @Volatile
        private var INSTANCE: SpeechToTextClient? = null

        fun getInstance(): SpeechToTextClient =
            INSTANCE ?: buildService().also {
                INSTANCE = it
            }

    companion object {
        private const val baseUrl = "https://api.diffchecker.com/public/"
    }
}
