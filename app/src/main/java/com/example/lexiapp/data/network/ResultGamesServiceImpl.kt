package com.example.lexiapp.data.network

import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.ResultGamesService
import javax.inject.Inject

class ResultGamesServiceImpl @Inject constructor() : ResultGamesService {
    override fun getWhereIsTheLetterResults(emails: List<String>): List<WhereIsTheLetterResult> {
        TODO("Not yet implemented")
    }

}
