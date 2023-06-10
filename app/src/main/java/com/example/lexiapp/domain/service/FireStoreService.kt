package com.example.lexiapp.domain.service

import com.example.lexiapp.data.model.GameResult
import com.example.lexiapp.data.model.WhereIsGameResult
import com.example.lexiapp.domain.model.User
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow

interface FireStoreService {

    suspend fun saveAccount(user: User)

    suspend fun getUser(email: String): User

    suspend fun saveWhereIsTheLetterResult(result: GameResult)

    suspend fun obtainLastResults(userMail: String): List<WhereIsGameResult>

    suspend fun getOpenAICollectionDocumentReference(document: String): Flow<DocumentReference>
}
