package com.example.lexiapp.data.model

import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult

data class WhereIsTheLetterDataResult(
    val result: Boolean,
    val mainLetter: String,
    val selectedLetter: String,
    val word: String
)

fun WhereIsTheLetterDataResult.toWhereIsTheLetterResult(email: String): WhereIsTheLetterResult {
    return WhereIsTheLetterResult(
        email = email,
        mainLetter = this.mainLetter.toCharArray()[0],
        selectedLetter = this.selectedLetter.toCharArray()[0],
        success = this.result,
        word = this.word
    )
}

fun WhereIsTheLetterResult.toWhereIsGameResult() : WhereIsTheLetterDataResult{
    return WhereIsTheLetterDataResult(
        result = this.success,
        mainLetter = this.mainLetter.toString(),
        selectedLetter = this.selectedLetter.toString(),
        word = this.word
    )
}