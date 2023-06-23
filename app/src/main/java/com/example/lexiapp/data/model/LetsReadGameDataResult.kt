package com.example.lexiapp.data.model

import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult

data class LetsReadGameDataResult(
    var email: String,
    val success: Boolean,
    val wrongWords: List<String>,
    val totalWords: Int,
    val date: String? = null
)

fun LetsReadGameResult.toLetsReadGameDataResult(): LetsReadGameDataResult {
    return LetsReadGameDataResult(
        email = this.email,
        success = this.success,
        wrongWords = this.wrongWords,
        totalWords = this.totalWords
    )
}

fun LetsReadGameDataResult.toLetsReadGameResult(): LetsReadGameResult {
    return LetsReadGameResult(
        email = this.email,
        success = this.success,
        wrongWords = this.wrongWords,
        totalWords = this.totalWords,
        date = this.date
    )
}
