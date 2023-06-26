package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
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

    private val letterChanges = mapOf(
        'I' to 'E',
        'E' to 'I',
        'B' to 'D',
        'D' to 'B',
        'P' to 'Q',
        'Q' to 'P',
        'M' to 'W',
        'W' to 'M'
    )

    fun getWords() = flow {

        getWord().collect { baseWord ->
            val words = mutableListOf<String>()

            words.add(baseWord)

            /*
            Change letters of the word for other letters with which people with dyslexia
             usually exchange visually, the list of these letters and with which
             they are exchanged are in the map letterChanges
            */
            for(letter in baseWord){
                val wordChanged = baseWord.replace(letter, letterChanges[letter] ?: letter)
                if(!words.contains(wordChanged) && words.size < 4) {
                    words.add(wordChanged)
                }
            }

            /*
            If 3 variations of the word are not achieved using the exchange of letters,
             the previous algorithm is used to complete the list
            */
            while(words.size < 4){
                val charArray = baseWord.toCharArray()
                val indicesToShuffle = charArray.indices.shuffled(Random(baseWord.length % NUM_OF_CHARS_SHUFFLE + words.size))
                var finalWord = ""
                for (indice in indicesToShuffle) {
                    finalWord += charArray[indice]
                }
                if (finalWord !in words && words.size < 4) {
                    words.add(finalWord)
                }
            }
            Log.i("Lucas", "$words")
            emit(words)
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
