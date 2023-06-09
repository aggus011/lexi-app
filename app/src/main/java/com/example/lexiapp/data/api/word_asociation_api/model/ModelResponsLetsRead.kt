package com.example.lexiapp.data.api.word_asociation_api.model

import com.google.gson.annotations.SerializedName
import java.io.File

data class Texts (
    @SerializedName("text")
    val text: String
)

data class SendInformation(
    val left: String,
    val right: String,
    val diff_level: String = "words"
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