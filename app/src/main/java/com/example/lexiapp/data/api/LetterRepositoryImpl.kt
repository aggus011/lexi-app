package com.example.lexiapp.data.api

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import com.example.lexiapp.data.model.Game
import com.example.lexiapp.data.model.GameResult
import com.example.lexiapp.data.model.WhereIsGameResult
import com.example.lexiapp.data.network.FireStoreService
import com.example.lexiapp.data.repository.BlackList
import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LetterRepositoryImpl @Inject constructor(
    private val apiWordService: WordAssociationService,
    private val db: FireStoreService,
    private val prefs: SharedPreferences
) {

    private val userMail = prefs.getString("email", null)!!
    private val currentGame = Game.WHERE_IS_THE_LETTER_GAME

    suspend fun getWord(count: Int, length: Int, language: String) = flow {
        apiWordService.getWordToWhereIsTheLetterGame(count, length, language)
            .map { inputList -> inputList.filter { !BlackList.words.contains(it.uppercase()) } }
            .collect {
                emit(it[0].uppercase())
                Log.v("data_in_repository", "response word: $it")
            }
    }

    suspend fun saveResult(result: WhereIsTheLetterResult) {
        db.saveWhereIsTheLetterResult(
            WhereIsGameResult(
                game = currentGame,
                user_mail = userMail,
                result = Pair(result.word.toString(), result.intents.toString())
            )
        )
    }

    fun obtainResults() {

    }

}