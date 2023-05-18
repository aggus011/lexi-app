package com.example.lexiapp.data.api

import java.util.concurrent.Flow

interface LetterRepository {
    suspend fun getWord(): kotlinx.coroutines.flow.Flow<String>
}