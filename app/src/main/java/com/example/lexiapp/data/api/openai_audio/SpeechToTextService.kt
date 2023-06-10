package com.example.lexiapp.data.api.openai_audio

import com.example.lexiapp.data.api.openai_audio.model.Texts
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpeechToTextService @Inject constructor(
    private val client: SpeechToTextClient
    ) {
    suspend fun transcription(path: MultipartBody.Part): Response<Texts> {
        val model = "whisper-1".toRequestBody("text/plain".toMediaTypeOrNull())
        val language = "es".toRequestBody("text/plain".toMediaTypeOrNull())

        return client.transcription(
            model = model,
            file = path,
            language = language
        )
    }
}
