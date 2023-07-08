package com.example.lexiapp.domain.model.gameResult

data class CorrectWordGameResult(
    override var email: String,
    val wordSelected: String,
    val correctWord: String,
    override val success: Boolean,
    override val date: String?
): ResultGame(email, success, date)
