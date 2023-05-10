package com.example.lexiapp.domain

import com.example.lexiapp.data.database.dao.UserDao
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getWordToLetterGame(): Flow<String?>
}