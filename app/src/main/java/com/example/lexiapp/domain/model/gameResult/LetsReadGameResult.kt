package com.example.lexiapp.domain.model.gameResult

data class LetsReadGameResult(
    override var email: String,
    override val success: Boolean,
    val wrongWords: List<String>,
    val totalWords: Int
): ResultGame(email, success)
