package com.example.lexiapp.data.api.openaicompletions.model

import com.google.gson.annotations.SerializedName

data class OpenAICompletionsRequest(
    @SerializedName("model")
    val model: String = "text-davinci-003",

    @SerializedName("prompt")
    val prompt: String,

    @SerializedName("max_tokens")
    val maxTokens: Int = 1000
)