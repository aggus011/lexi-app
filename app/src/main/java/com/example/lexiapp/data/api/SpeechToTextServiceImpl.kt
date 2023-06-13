package com.example.lexiapp.data.api

import com.example.lexiapp.data.api.openai_audio.SpeechToTextGateway
import com.example.lexiapp.data.api.openai_audio.model.Texts
import com.example.lexiapp.domain.service.SpeechToTextService
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class SpeechToTextServiceImpl @Inject constructor(private val apiService: SpeechToTextGateway): SpeechToTextService {

    override suspend fun transcription(file: MultipartBody.Part): Response<Texts> =
        apiService.transcription(file)
}