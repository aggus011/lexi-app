package com.example.lexiapp.data.model

import com.example.lexiapp.data.api.word_asociation_api.WordAssociationClient
import com.example.lexiapp.domain.model.WhereIsTheLetterResult

data class WhereIsGameResult(
    val result: Boolean,
    val mainLetter: String,
    val selectedLetter: String,
    val word: String
)

fun WhereIsGameResult.toWhereIsTheLetterResult(email: String): WhereIsTheLetterResult {
    return WhereIsTheLetterResult(
        email = email,
        mainLetter = this.mainLetter.toCharArray()[0],
        selectedLetter = this.selectedLetter.toCharArray()[0],
        success = this.result,
        word = this.word
    )
}