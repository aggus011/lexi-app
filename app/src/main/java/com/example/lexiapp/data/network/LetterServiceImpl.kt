package com.example.lexiapp.data.network

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationGateway
import com.example.lexiapp.data.model.toCorrectWordDataResult
import com.example.lexiapp.data.model.toWhereIsTheLetterDataResult
import com.example.lexiapp.data.repository.BlackList
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.ResultGame
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.LetterService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LetterServiceImpl @Inject constructor(
    private val apiWordService: WordAssociationGateway,
    private val db: FireStoreServiceImpl,
    private val prefs: SharedPreferences,
    private val notificationServiceImpl: FirebaseNotificationServiceImpl
) : LetterService {

    private val userMail = prefs.getString("email", null)!!

    override suspend fun getWord(count: Int, length: Int, language: String) = flow {
        val errorWords = db.getWordPlayed(userMail)
        Log.d("WORDS_ERROR", "${errorWords.first}//${errorWords.second}")
        val stimulus = if (errorWords.first) errorWords.second else db.getWordCategories(userMail)
        apiWordService.getWordToWhereIsTheLetterGame(count, length, language, stimulus)
            .map { inputList -> inputList.filter { !BlackList.words.contains(it.uppercase()) } }
            .collect {
                emit(it[0].uppercase())
            }
    }

    override suspend fun saveResult(result: ResultGame) {
        saveProgress(result)
        Log.v("SAVE_ANSWER_LETTER_IMPL", "${result.success}")
        when(result::class){
            WhereIsTheLetterResult::class -> {
                Log.v("SAVE_ANSWER_LETTER_IMPL", "${result.success}//${result.email}")
                val witlRes=(result as WhereIsTheLetterResult).toWhereIsTheLetterDataResult()
                Log.v("SAVE_ANSWER_LETTER_IMPL", "${witlRes.result}//${witlRes.word}//${witlRes.selectedLetter}")
                db.saveWhereIsTheLetterResult(witlRes, result.email)
            }
            CorrectWordGameResult::class -> {
                db.saveCorrectWordResult((result as CorrectWordGameResult).toCorrectWordDataResult(), result.email)
            }
        }
    }

    override suspend fun getWordsForChallengeReading(
        count: Int,
        length: Int,
        language: String
    ) = flow {
        val errorWords = db.getWordPlayed(userMail)
        Log.d("WORDS_ERROR", "${errorWords.first}//${errorWords.second}")
        val stimulus = if (errorWords.first) errorWords.second else db.getWordCategories(userMail)
        apiWordService.getWordToWhereIsTheLetterGame(count, length, language, stimulus)
            .map { inputList -> inputList.filter { !BlackList.words.contains(it.uppercase()) } }
            .collect {
                emit(it.take(count))
            }
    }

    private suspend fun saveProgress(result: ResultGame){
        when(result::class){
            WhereIsTheLetterResult::class -> {
                if (result.success) {
                    db.updateObjectiveProgress("WL", "hit")
                }
                db.updateObjectiveProgress("WL", "play")
            }
            CorrectWordGameResult::class -> {
                if (result.success) {
                    db.updateObjectiveProgress("CW", "hit")
                }
                db.updateObjectiveProgress("CW", "play")
            }
        }
    }

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

    private suspend fun getErrorWords(){
        var errorWords = Pair(false,listOf<String>())

    }
}
