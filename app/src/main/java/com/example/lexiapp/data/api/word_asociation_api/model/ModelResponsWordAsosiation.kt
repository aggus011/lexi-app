package com.example.lexiapp.data.api.word_asociation_api.model

import com.google.gson.annotations.SerializedName

data class WordAssociation(
    @SerializedName("response")
    val wordsAsociate: ArrayList<Items>
)

data class Items(
    @SerializedName("items")
    val blocks: List<Item>
)

data class Item(
    @SerializedName("item")
    val word: String
)