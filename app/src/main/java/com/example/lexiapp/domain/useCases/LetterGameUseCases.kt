package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import com.example.lexiapp.data.model.toWhereIsTheLetterDataResult
import com.example.lexiapp.domain.exceptions.OversizeException
import com.example.lexiapp.domain.model.gameResult.ResultGame
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.LetterService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class LetterGameUseCases @Inject constructor(
    private val service: LetterService,
    private val prefs: SharedPreferences
) {
    private val SPANISH_LANGUAGE = "es"
    private var MAX_LENGHT = 7
    private val ONE_WORD = 1

    fun getWord() = flow {
        //MAX_LENGHT = getRandomInt()
        service.getWord(ONE_WORD, MAX_LENGHT, SPANISH_LANGUAGE).collect {
            val word = it.split(" ")[0]
            if (word.length in 3..7) {
                emit(word)
            } else {
                throw OversizeException("size: ${it.length}")
            }
        }
    }

    suspend fun saveWordInFirebase(result: WhereIsTheLetterResult) {
        result.email = prefs.getString("email", null).toString()
        Log.v("SAVE_ANSWER_UC", "${result.success}")
        service.saveResult(result)
    }

    private fun getRandomInt() = Random.nextInt(4, 7)
}