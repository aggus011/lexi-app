package com.example.lexiapp.data.word_asociation_api.model

import com.google.gson.annotations.SerializedName

data class WordAssociationResponse(
    @SerializedName("response")
    val wordsAsociate: List<Association>
)

data class Association(
    @SerializedName("text")
    val word: String
)