package com.example.lexiapp.data.network

import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.ResultGamesService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResultGamesServiceImpl @Inject constructor(
    private val db: FireStoreService
) : ResultGamesService {
    override suspend fun getWhereIsTheLetterResults(email: String) =
        db.getLastResultsWhereIsTheLetterGame(email)

    override suspend fun getCorrectWordResults(email: String) = flow {
        db.getLastResultsCorrectWordGame(email).collect {
            emit(it)
        }
    }
}
