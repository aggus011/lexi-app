package com.example.lexiapp.domain.model

data class WhereIsTheLetterResult(
    var email: String,
    val mainLetter: Char,
    val selectedLetter: Char,
    val word: String,
    val success: Boolean
)