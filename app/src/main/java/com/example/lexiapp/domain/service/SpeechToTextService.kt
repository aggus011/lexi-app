package com.example.lexiapp.domain.service

import com.example.lexiapp.data.api.openai_audio.model.Texts
import okhttp3.MultipartBody
import retrofit2.Response

interface SpeechToTextService {

    suspend fun transcription(file: MultipartBody.Part): Response<Texts>
}