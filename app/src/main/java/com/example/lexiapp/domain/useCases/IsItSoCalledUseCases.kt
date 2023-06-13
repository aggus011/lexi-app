package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.service.LetterService
import javax.inject.Inject

class IsItSoCalledUseCases @Inject constructor(
    private val repository: LetterService
) {
}