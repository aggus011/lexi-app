package com.example.lexiapp.data.api

import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.data.api.difference_text.model.SendInformation
import com.example.lexiapp.data.api.openai_audio.SpeechToTextService
import com.example.lexiapp.data.api.openai_audio.model.Texts
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class SpeechToTextRepository @Inject constructor(private val apiService: SpeechToTextService) {

    suspend fun transcription(file: MultipartBody.Part): Response<Texts> =
        apiService.transcription(file)
}