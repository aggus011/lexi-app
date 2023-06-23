package com.example.lexiapp.domain.model.gameResult

data class LetsReadGameResult(
    override var email: String,
    override val success: Boolean,
    val wrongWords: List<String>,
    val totalWords: Int,
    override val date: String?=null
): ResultGame(email, success, date)
