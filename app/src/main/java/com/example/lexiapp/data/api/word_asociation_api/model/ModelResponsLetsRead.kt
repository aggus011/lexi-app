package com.example.lexiapp.data.api.word_asociation_api.model

import java.io.File

data class TranscriptionInformation (
    val model: String = "whisper-1",
    val language: String = "es"
)

data class Texts (
    val text: String
)

data class Rows(
    val rows: List<DifferenceInformation>,
    val added: Int,
    val removed: Int
)

data class DifferenceInformation(
    val end: Boolean,
    val left: Chunks,
    val right: Chunks,
    val insideChanged: Boolean,
    var start: Boolean
)

data class Chunks(
    val chunks : List<Results>
)

data class Results(
    val value: String,
    val type: String
)

data class SendInformation(
    val left: String,
    val right: String,
    val diff_level: String = "words"
)