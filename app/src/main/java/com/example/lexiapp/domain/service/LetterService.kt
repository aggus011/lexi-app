package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import kotlinx.coroutines.flow.Flow

interface LetterService {

    suspend fun getWord(count: Int, length: Int, language: String): Flow<String>

    suspend fun saveResult(result: WhereIsTheLetterResult)

}