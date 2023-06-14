package com.example.lexiapp.data.api

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import com.example.lexiapp.data.model.Game
import com.example.lexiapp.data.model.WhereIsGameResult
import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.data.repository.BlackList
import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.LetterService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LetterServiceImpl @Inject constructor(
    private val apiWordService: WordAssociationService,
    private val db: FireStoreServiceImpl,
    private val prefs: SharedPreferences
) : LetterService {

    private val userMail = prefs.getString("email", null)!!
    private val currentGame = Game.WHERE_IS_THE_LETTER_GAME

    override suspend fun getWord(count: Int, length: Int, language: String) = flow {
        apiWordService.getWordToWhereIsTheLetterGame(count, length, language)
            .map { inputList -> inputList.filter { !BlackList.words.contains(it.uppercase()) } }
            .collect {
                emit(it[0].uppercase())
                Log.v("data_in_repository", "response word: $it")
            }
    }

    override suspend fun saveResult(result: WhereIsTheLetterResult) {
        db.saveWhereIsTheLetterResult(result.toWhereIsGameResult(), userMail)
    }

    override fun obtainResults() {

    }

}

private fun WhereIsTheLetterResult.toWhereIsGameResult(): WhereIsGameResult {
    return WhereIsGameResult(
        result = this.success,
        mainLetter = this.mainLetter.toString(),
        selectedLetter = this.selectedLetter.toString(),
        word = this.word
    )
}
