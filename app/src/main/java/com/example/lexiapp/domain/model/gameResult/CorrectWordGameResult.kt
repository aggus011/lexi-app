package com.example.lexiapp.domain.model.gameResult

import com.example.lexiapp.data.model.WhereIsTheLetterDataResult

data class CorrectWordGameResult(
    override var email: String,
    val wordSelected: String,
    val correctWord: String,
    override val success: Boolean,
    override val date: String?
): ResultGame(email, success, date)
