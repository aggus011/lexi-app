package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult
import com.example.lexiapp.domain.service.DifferenceService
import com.example.lexiapp.domain.service.FireStoreService
import retrofit2.Response
import javax.inject.Inject

class DifferenceUseCases @Inject constructor(
    private val service: DifferenceService,
    private val prefs: SharedPreferences
){

    suspend fun getDifference(originalText: String, revisedText: String) : Response<Rows> =
        service.getDifference(originalText, revisedText)

    suspend fun saveWrongWords(result: LetsReadGameResult) {
        prefs.getString("email", null)?.let { result.email = it }
        service.saveLetsReadResult(result)
    }

}