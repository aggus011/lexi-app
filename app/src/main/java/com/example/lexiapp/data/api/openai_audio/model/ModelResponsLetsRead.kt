package com.example.lexiapp.data.api.openai_audio.model

import com.google.gson.annotations.SerializedName

data class Texts (
    @SerializedName("text")
    val text: String
)