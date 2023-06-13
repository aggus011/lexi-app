package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.service.SpeechToTextService
import okhttp3.MultipartBody
import javax.inject.Inject

class SpeechToTextUseCases @Inject constructor(
    private val repository: SpeechToTextService
){

    suspend fun transcription(audioPart: MultipartBody.Part) =
        repository.transcription(audioPart)
}