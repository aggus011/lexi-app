package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.service.LetterService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class CorrectWordUseCases @Inject constructor(
    private val repository: LetterService
) {
    companion object {
        const val NUM_OF_CHARS_SHUFFLE = 2
    }

    fun getWords() = flow {
        getWord().collect { input ->
            val words = mutableListOf(input)
            val charArray = input.toCharArray()
            for (i in 0..3) {
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
        repository.getWord(WORD_COUNT, LENGTH_WORD, SPANISH_LANGUAGE)
            .collect { emit(it) }
    }

    private fun getRandomInt() = Random.nextInt(4, 7)

}
