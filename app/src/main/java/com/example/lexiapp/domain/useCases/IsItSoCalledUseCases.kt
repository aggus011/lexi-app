package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.service.LetterRepository
import javax.inject.Inject

class IsItSoCalledUseCases @Inject constructor(
    private val repository: LetterRepository
) {
}