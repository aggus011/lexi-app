package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.api.LetterRepositoryImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class LetterGameUseCases @Inject constructor(
    private val repository: LetterRepositoryImpl
) {
    private val SPANISH_LANGUAGE = "es"
    private var LENGTH_WORD = getRandomInt()
    private val ONE_WORD = 1

    fun getWord() = flow {
        LENGTH_WORD=getRandomInt()
        repository.getWord(ONE_WORD, LENGTH_WORD, SPANISH_LANGUAGE).collect { emit(it) }
    }

    private fun getRandomInt()=Random.nextInt(4,7)
}