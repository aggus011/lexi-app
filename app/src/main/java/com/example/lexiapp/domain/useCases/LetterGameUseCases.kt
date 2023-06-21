package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.exceptions.OversizeException
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.LetterService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class LetterGameUseCases @Inject constructor(
    private val service: LetterService,
    private val db: FireStoreService,
    private val prefs: SharedPreferences
) {
    private val SPANISH_LANGUAGE = "es"
    private var LENGTH_WORD = getRandomInt()
    private val ONE_WORD = 1

    fun getWord() = flow {
        LENGTH_WORD = getRandomInt()
        service.getWord(ONE_WORD, LENGTH_WORD, SPANISH_LANGUAGE).collect {
            val word = it.split(" ")[0]
            if (word.length in 4..7) {
                emit(word)
            } else {
                throw OversizeException("size: ${it.length}")
            }
        }
    }

    suspend fun saveWordInFirebase(result: WhereIsTheLetterResult) {
        result.email = prefs.getString("email", null).toString()
        service.saveResult(result)
    }

    private fun getRandomInt() = Random.nextInt(4, 7)
}