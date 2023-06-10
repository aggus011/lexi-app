package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.api.SpeechToTextRepository
import com.example.lexiapp.data.api.openai_audio.model.Texts
import okhttp3.MultipartBody
import okhttp3.Response
import javax.inject.Inject

class SpeechToTextUseCases @Inject constructor(
    private val repository: SpeechToTextRepository
){

    suspend fun transcription(audioPart: MultipartBody.Part) =
        repository.transcription(audioPart)
}