package com.example.lexiapp.data.model

import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult

data class CorrectWordDataResult(
    val result: Boolean,
    val mainWord: String,
    val selectedWord: String
)

fun CorrectWordGameResult.toCorrectWordDataResult(): CorrectWordDataResult {
    return CorrectWordDataResult(
        result = this.success,
        mainWord = this.correctWord,
        selectedWord = this.wordSelected
    )
}