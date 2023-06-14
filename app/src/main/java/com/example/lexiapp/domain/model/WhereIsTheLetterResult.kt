package com.example.lexiapp.domain.model

data class WhereIsTheLetterResult(
    val mainLetter: Char,
    val selectedLetter: Char,
    val word: String,
    val success: Boolean
)