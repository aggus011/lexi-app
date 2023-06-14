package com.example.lexiapp.domain.service

import com.example.lexiapp.data.model.WhereIsGameResult
import com.example.lexiapp.domain.model.Professional
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import java.util.*

interface FireStoreService {

    suspend fun saveAccount(user: User)

    suspend fun getUser(email: String): User

    suspend fun saveWhereIsTheLetterResult(result: WhereIsGameResult, email: String)

    suspend fun obtainLastResults(userMail: String): List<WhereIsTheLetterResult>

    suspend fun getOpenAICollectionDocumentReference(document: String): Flow<DocumentReference>

    suspend fun saveProfessionalAccount(professional: Professional, registrationDate: Date)

    suspend fun getProfessional(email: String): Professional
}
