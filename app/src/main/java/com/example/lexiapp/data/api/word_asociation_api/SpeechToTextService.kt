package com.example.lexiapp.data.api.word_asociation_api

import com.example.lexiapp.data.api.word_asociation_api.model.Texts
import com.example.lexiapp.data.api.word_asociation_api.model.TranscriptionInformation
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Inject

class SpeechToTextService @Inject constructor() {
    suspend fun transcription(path: File): Response<Texts> {
        val information = TranscriptionInformation(file = path)
        return serviceGetTextFromPrompt.transcription(information)
    }


    private val serviceGetTextFromPrompt: SpeechToTextClient = Retrofit.Builder()
        .baseUrl("https://api.openai.com/v1/audio/")
        //.client(clientOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SpeechToTextClient::class.java)
}
