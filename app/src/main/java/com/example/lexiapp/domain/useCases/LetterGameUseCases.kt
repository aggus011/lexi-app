package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.LetterService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class LetterGameUseCases @Inject constructor(
    private val service: LetterService,
    private val db: FireStoreService
) {
    private val SPANISH_LANGUAGE = "es"
    private var LENGTH_WORD = getRandomInt()
    private val ONE_WORD = 1

    fun getWord() = flow {
        LENGTH_WORD = getRandomInt()
        service.getWord(ONE_WORD, LENGTH_WORD, SPANISH_LANGUAGE).collect { emit(it) }
    }

    suspend fun saveWordInFirebase(result: WhereIsTheLetterResult) {
        service.saveResult(result)
    }

    private fun getRandomInt() = Random.nextInt(4, 7)
}