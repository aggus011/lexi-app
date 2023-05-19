package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.api.LetterRepositoryImpl
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LetterGameUseCases @Inject constructor(
    private val repository: LetterRepositoryImpl
) {
    fun getWord() = flow {
        repository.getWord().collect { emit(it) }
    }

}