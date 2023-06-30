package com.example.lexiapp.data.api

import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.data.network.FirebaseNotificationServiceImpl
import com.example.lexiapp.domain.service.TextScannerService
import javax.inject.Inject

class TextScannerServiceImpl @Inject constructor(
    private val db: FireStoreServiceImpl,
    private val notificationServiceImpl: FirebaseNotificationServiceImpl
) : TextScannerService {

    override suspend fun generateNotificationIfObjectiveHasBeenCompleted(
        game: String,
        typeGame: String,
        gameName: String
    ) {
        val patientToken = db.getDeviceToken()

        val objectivesPair = db.checkIfObjectiveAndWeeklyObjectivesWereCompleted(game, typeGame)

        if (objectivesPair.first && objectivesPair.second) {
            notificationServiceImpl.sendNotificationWeeklyObjectivesCompleted(patientToken)
        } else if (objectivesPair.first) {
            notificationServiceImpl.sendNotificationObjectiveCompleted(
                patientToken,
                if (typeGame == "hit") "Acertar en $gameName" else "Jugar $gameName"
            )
        }
    }
}