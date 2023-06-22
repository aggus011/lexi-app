package com.example.lexiapp.domain.service

import com.example.lexiapp.data.model.CorrectWordDataResult
import com.example.lexiapp.data.model.WhereIsTheLetterDataResult
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Professional
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.model.*
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import java.util.*

interface FireStoreService {

    suspend fun saveAccount(user: User)

    suspend fun getUser(email: String): User

    suspend fun saveWhereIsTheLetterResult(result: WhereIsTheLetterDataResult, email: String)

    suspend fun getLastResultsWhereIsTheLetterGame(userMail: String): Flow<List<WhereIsTheLetterResult>>

    suspend fun getLastResultsCorrectWordGame(userMail: String): Flow<List<CorrectWordGameResult>>

    suspend fun getOpenAICollectionDocumentReference(document: String): Flow<DocumentReference>

    suspend fun saveProfessionalAccount(professional: Professional, registrationDate: Date)

    suspend fun getProfessional(email: String): Professional

    suspend fun getIsLinked(email: String): Boolean?

    suspend fun bindProfessionalToPatient (emailPatient: String, emailProfessional: String): FirebaseResult

    suspend fun addPatientToProfessional (emailPatient: String, emailProfessional: String): CompletableDeferred<FirebaseResult>

    suspend fun getListLinkPatientOfProfessional (emailProfessional: String, listener: (List<String>?) -> Unit )

    suspend fun unBindProfessionalFromPatient (emailPatient: String): FirebaseResult

    suspend fun deletePatientFromProfessional (emailPatient: String, emailProfessional: String): CompletableDeferred<FirebaseResult>
    suspend fun saveCorrectWordResult(result: CorrectWordDataResult, email: String)

    suspend fun saveObjectives(email: String, objectives: List<Objective>)

    suspend fun checkObjectivesExist(email: String): Boolean

    suspend fun getObjectives(email: String): List<Objective>

    suspend fun saveCategoriesFromPatient(email: String, categories: List<String>)

    suspend fun getPatientCategories(email: String): List<String>
}
