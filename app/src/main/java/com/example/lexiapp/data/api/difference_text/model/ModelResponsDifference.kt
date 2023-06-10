package com.example.lexiapp.data.api.difference_text.model

import com.google.gson.annotations.SerializedName

data class Rows(
    @SerializedName("rows")
    val rows: List<DifferenceInformation>,
    @SerializedName("added")
    val added: Int,
    @SerializedName("removed")
    val removed: Int
)

data class DifferenceInformation(
    @SerializedName("end")
    val end: Boolean,
    @SerializedName("left")
    val left: Chunks,
    @SerializedName("right")
    val right: Chunks,
    @SerializedName("insideChanged")
    val insideChanged: Boolean,
    @SerializedName("start")
    var start: Boolean
)

data class Chunks(
    @SerializedName("chunks")
    val chunks : List<Results>
)

data class Results(
    @SerializedName("value")
    val value: String,
    @SerializedName("type")
    val type: String
)
