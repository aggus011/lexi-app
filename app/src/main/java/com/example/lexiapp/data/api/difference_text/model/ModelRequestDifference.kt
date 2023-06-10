package com.example.lexiapp.data.api.difference_text.model

import com.google.gson.annotations.SerializedName

data class SendInformation(
    @SerializedName("left")
    val left: String,
    @SerializedName("right")
    val right: String,
    @SerializedName("diff_level")
    val diff_level: String = "words"
)