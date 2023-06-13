package com.example.lexiapp.data.api.openai_audio

import com.example.lexiapp.data.api.openai_audio.model.Texts
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class SpeechToTextGateway @Inject constructor(
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
