package com.example.lexiapp.data.network

import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.ResultGamesService
import javax.inject.Inject

class ResultGamesServiceImpl @Inject constructor(
    private val db: FireStoreService
) : ResultGamesService {
    override suspend fun getWhereIsTheLetterResults(email: String) =
        db.getLastResultsWhereIsTheLetterGame(email)

}