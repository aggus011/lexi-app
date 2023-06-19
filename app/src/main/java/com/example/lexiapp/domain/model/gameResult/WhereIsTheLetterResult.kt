package com.example.lexiapp.domain.model.gameResult

data class WhereIsTheLetterResult(
    override var email: String,
    val mainLetter: Char,
    val selectedLetter: Char,
    val word: String,
    override val success: Boolean
): ResultGame(email, success)