package com.example.lexiapp.data.api

import com.example.lexiapp.data.api.word_asociation_api.SpeechToTextService
import com.example.lexiapp.data.api.word_asociation_api.model.Rows
import com.example.lexiapp.data.api.word_asociation_api.model.SendInformation
import com.example.lexiapp.data.api.word_asociation_api.model.Texts
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class SpeechToTextRepository @Inject constructor(private val apiService: SpeechToTextService) {

    suspend fun transcription(file: MultipartBody.Part): Response<Texts> =
        apiService.transcription(file)


    suspend fun getDifference(originalText: String, revisedText: String) : Response<Rows>{
        val sendInformation = SendInformation(originalText, revisedText)
        return apiService.getDifference(sendInformation)
    }
}