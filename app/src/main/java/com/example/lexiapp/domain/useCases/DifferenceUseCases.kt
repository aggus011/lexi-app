package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult
import com.example.lexiapp.domain.service.DifferenceService
import retrofit2.Response
import javax.inject.Inject

class DifferenceUseCases @Inject constructor(
    private val service: DifferenceService,
    private val prefs: SharedPreferences
){

    suspend fun getDifference(originalText: String, revisedText: String) : Response<Rows> =
        service.getDifference(originalText, revisedText)

    suspend fun saveWrongWords(result: LetsReadGameResult, isChallengeReading: Int) {
        prefs.getString("email", null)?.let { result.email = it }
        service.saveLetsReadResult(result, isChallengeReading==1)
    }

    suspend fun generateNotificationForObjectives(game: String, type: String, gameName: String){
        service.generateNotificationIfObjectiveHasBeenCompleted(game, type, gameName)
    }

    fun normalizeWords(wrongWords: List<String>)= wrongWords
        .flatMap { it.split(" ") }
        .map { it.replace(Regex("[^a-zA-ZáéíóúÁÉÍÓÚ]"), "").uppercase() }
        .distinct()

}