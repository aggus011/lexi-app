package com.example.lexiapp.domain.service

import com.example.lexiapp.data.model.CorrectWordDataResult
import com.example.lexiapp.data.model.LetsReadGameDataResult
import com.example.lexiapp.data.model.WhereIsTheLetterDataResult
import com.example.lexiapp.domain.model.*
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
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

    suspend fun getListLinkPatientOfProfessional (emailProfessional: String): Flow<List<String>>

    suspend fun unBindProfessionalFromPatient (emailPatient: String): FirebaseResult

    suspend fun deletePatientFromProfessional (emailPatient: String, emailProfessional: String): CompletableDeferred<FirebaseResult>

    suspend fun saveCorrectWordResult(result: CorrectWordDataResult, email: String)

    suspend fun saveObjectives(email: String, objectives: List<Objective>)

    suspend fun checkObjectivesExist(email: String, lastMondayDate: String): Boolean

    suspend fun getObjectives(email: String, lastMondayDate: String, listener: (List<Objective>) -> Unit)

    suspend fun saveLetsReadResult(result: LetsReadGameDataResult)

    suspend fun saveNote (note: Note): Flow<FirebaseResult>

    suspend fun deleteNote (emailPatient: String, date: String): Flow<FirebaseResult>

    suspend fun getNotes (emailPatient: String): Flow<List<Note>>

    suspend fun saveCategoriesFromPatient(email: String, categories: List<String>)

    suspend fun getPatientCategories(email: String): List<String>

    suspend fun saveTokenToPatient(emailPatient: String)

    suspend fun saveTokenToProfessional(emailProfessional: String)

    suspend fun getDeviceToken(): String

    suspend fun getPatientToken(patientEmail: String): String?

    suspend fun getProfessionalToken(professionalEmail: String): String?

    suspend fun getWordPlayed(email: String): Pair<Boolean, List<String>>

    suspend fun getWordCategories(email: String): List<String>

    suspend fun updateObjectiveProgress(game: String, type: String)

    suspend fun getObjectivesHistory(uid: String): Flow<List<MiniObjective>>

    suspend fun checkIfObjectiveAndWeeklyObjectivesWereCompleted(game: String, type: String): Pair<Boolean, Boolean>
}
