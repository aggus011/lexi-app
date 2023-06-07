package com.example.lexiapp.data.api.openaicompletions.model

import com.google.gson.annotations.SerializedName

data class OpenAICompletionsResponse(
    @SerializedName("choices")
    val choiceList: List<Choice>,

    @SerializedName("usage")
    val usage: Usage
)

data class Choice(
    @SerializedName("text")
    val text: String
)

data class Usage(
    @SerializedName("total_tokens")
    val totalToken: Int
)
