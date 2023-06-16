package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.ResultGamesService
import javax.inject.Inject

class ResultGamesUseCases @Inject constructor(
    private val service: ResultGamesService
) {

    fun getWhereIsTheLetterResults(emails: List<String>): List<WhereIsTheLetterResult> {
        return service.getWhereIsTheLetterResults(emails)
    }
}
