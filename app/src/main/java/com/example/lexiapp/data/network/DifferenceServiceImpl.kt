package com.example.lexiapp.data.network

import com.example.lexiapp.data.api.difference_text.DifferenceGateway
import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.data.api.difference_text.model.SendInformation
import com.example.lexiapp.data.model.toLetsReadGameDataResult
import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult
import com.example.lexiapp.domain.service.DifferenceService
import com.example.lexiapp.domain.service.FireStoreService
import retrofit2.Response
import javax.inject.Inject

class DifferenceServiceImpl @Inject constructor(
    private val apiDifferenceGateway: DifferenceGateway,
    private val db: FireStoreService,
    private val notificationServiceImpl: FirebaseNotificationServiceImpl
) : DifferenceService {

    override suspend fun getDifference(originalText: String, revisedText: String) : Response<Rows> {
        val sendInformation = SendInformation(originalText, revisedText)
        return apiDifferenceGateway.getDifference(sendInformation)
    }

    override suspend fun saveLetsReadResult(
        result: LetsReadGameResult,
        isChallengeReading: Boolean
    ) {
        db.saveLetsReadResult(result.toLetsReadGameDataResult())
        saveProgress(result, isChallengeReading)
    }

    override suspend fun generateNotificationIfObjectiveHasBeenCompleted(
        game: String,
        typeGame: String,
        gameName: String
    ) {
        val patientToken = db.getDeviceToken()

        val objectivesPair = db.checkIfObjectiveAndWeeklyObjectivesWereCompleted(game, typeGame)

        if(objectivesPair.first && objectivesPair.second){
            notificationServiceImpl.sendNotificationWeeklyObjectivesCompleted(patientToken)
        }else if(objectivesPair.first){
            notificationServiceImpl.sendNotificationObjectiveCompleted(patientToken,
                if(typeGame == "hit") "Acertar en $gameName" else "Jugar $gameName")
        }
    }

    private suspend fun saveProgress(result: LetsReadGameResult, isChallengeReading: Boolean) {
        if (isChallengeReading) {
            updateChallengeReading(result)
        } else {
            updateNotChallengeReading(result)
        }
    }

    private suspend fun updateChallengeReading(result: LetsReadGameResult) {
        if (result.success) {
            db.updateObjectiveProgress("RC", "hit")
        }
        db.updateObjectiveProgress("RC", "play")
    }

    private suspend fun updateNotChallengeReading(result: LetsReadGameResult) {
        if (result.success) {
            db.updateObjectiveProgress("LR", "hit")
        }
        db.updateObjectiveProgress("LR", "play")
    }
}