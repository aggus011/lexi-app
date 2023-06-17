package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.WhereIsTheLetterResult

interface ResultGamesService {

    suspend fun getWhereIsTheLetterResults(email: String): List<WhereIsTheLetterResult>

}
