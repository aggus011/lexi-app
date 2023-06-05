package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.api.LetterRepositoryImpl
import javax.inject.Inject

class IsItSoCalledUseCases @Inject constructor(
    private val repository: LetterRepositoryImpl
) {
}