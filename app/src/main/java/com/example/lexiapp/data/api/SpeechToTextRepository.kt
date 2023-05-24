package com.example.lexiapp.data.api

import com.example.lexiapp.data.api.word_asociation_api.SpeechToTextService
import com.example.lexiapp.data.api.word_asociation_api.model.Texts
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class SpeechToTextRepository @Inject constructor(private val apiService: SpeechToTextService) {

    suspend fun transcription(file: File): Response<Texts> {
        return apiService.transcription(file)
    }
}