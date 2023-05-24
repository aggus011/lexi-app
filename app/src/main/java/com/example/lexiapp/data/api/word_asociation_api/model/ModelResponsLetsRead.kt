package com.example.lexiapp.data.api.word_asociation_api.model

import java.io.File

data class TranscriptionInformation (
    val model: String = "whisper-1",
    val file: File,
    val language: String = "spanish"
)

data class Texts (
    val text: String
)