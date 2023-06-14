package com.example.lexiapp.domain.service

import com.example.lexiapp.data.model.GameResult
import com.example.lexiapp.data.model.WhereIsGameResult
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.model.Professional
import com.example.lexiapp.domain.model.User
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.Flow
import java.util.*

interface FireStoreService {

    suspend fun saveAccount(user: User)

    suspend fun getUser(email: String): User

    suspend fun saveWhereIsTheLetterResult(result: GameResult)

    suspend fun obtainLastResults(userMail: String): List<WhereIsGameResult>

    suspend fun getOpenAICollectionDocumentReference(document: String): Flow<DocumentReference>

    suspend fun saveProfessionalAccount(professional: Professional, registrationDate: Date)

    suspend fun getProfessional(email: String): Professional

    suspend fun saveObjectives(email: String, objectives: List<Objective>)

    suspend fun checkObjectivesExist(email: String): Boolean

    suspend fun getObjectives(email: String): List<Objective>


}
