package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.ResultGamesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResultGamesUseCases @Inject constructor(
    private val service: ResultGamesService
) {

    suspend fun getWhereIsTheLetterResults(email: String): Flow<List<WhereIsTheLetterResult>> {
        return service.getWhereIsTheLetterResults(email)
    }

    suspend fun getWhereIsCWResults(email: String) = flow {
        service.getCorrectWordResults(email).collect{
            emit(it)
        }
    }
}
