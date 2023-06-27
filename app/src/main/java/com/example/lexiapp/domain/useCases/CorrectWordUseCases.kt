package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.service.LetterService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class CorrectWordUseCases @Inject constructor(
    private val service: LetterService,
    private val prefs: SharedPreferences
) {
    companion object {
        const val NUM_OF_CHARS_SHUFFLE = 2
    }

    fun getWords() = flow {
        getWord().collect { input ->
            val words = mutableListOf(input)
            val charArray = input.toCharArray()
            for (i in 0..2) {
                val indicesToShuffle = charArray.indices.shuffled(Random(input.length % NUM_OF_CHARS_SHUFFLE + i))
                var finalWord = ""
                for (indice in indicesToShuffle) {
                    finalWord += charArray[indice]
                }
                words.add(finalWord)
            }
            emit(words.toList())
        }
    }

    private val SPANISH_LANGUAGE = "es"
    private var LENGTH_WORD = getRandomInt()
    private val WORD_COUNT = 1

    fun getWord() = flow {
        LENGTH_WORD = getRandomInt()
        service.getWord(WORD_COUNT, LENGTH_WORD, SPANISH_LANGUAGE)
            .collect { emit(it) }
    }

    private fun getRandomInt() = Random.nextInt(4, 7)

    suspend fun saveAnswer(result: CorrectWordGameResult) {
        result.email = prefs.getString("email", null).toString()
        service.saveResult(result)
    }

}
