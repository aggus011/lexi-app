package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import kotlinx.coroutines.flow.Flow

interface

ResultGamesService {

    suspend fun getWhereIsTheLetterResults(email: String): Flow<List<WhereIsTheLetterResult>>

    suspend fun getCorrectWordResults(email: String): Flow<List<CorrectWordGameResult>>

}
