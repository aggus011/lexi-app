package com.example.lexiapp.domain.service

import com.example.lexiapp.data.model.WhereIsTheLetterDataResult
import com.example.lexiapp.domain.model.gameResult.ResultGame
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import kotlinx.coroutines.flow.Flow

interface LetterService {

    suspend fun getWord(count: Int, length: Int, language: String): Flow<String>

    suspend fun saveResult(result: ResultGame)

}