package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.api.LetterRepository
import javax.inject.Inject

class LetterGameUseCases @Inject constructor(
    private val repository: LetterRepository
) {

}