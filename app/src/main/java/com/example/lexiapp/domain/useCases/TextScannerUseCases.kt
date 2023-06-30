package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.TextScannerService
import javax.inject.Inject

class TextScannerUseCases @Inject constructor(
    private val firestore: FireStoreService,
    private val prefs: SharedPreferences,
    private val service: TextScannerService
){
    private val userEmail = prefs.getString("email", null)!!

    suspend fun saveTSResult() {
        firestore.saveTextScanResult(userEmail)
        firestore.updateObjectiveProgress("SCAN", "play")
    }

    suspend fun generateNotificationForObjectives(game: String, type: String, gameName: String){
        service.generateNotificationIfObjectiveHasBeenCompleted(game, type, gameName)
    }
}