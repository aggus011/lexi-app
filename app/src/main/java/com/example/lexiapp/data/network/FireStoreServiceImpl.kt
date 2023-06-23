package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.data.model.CorrectWordDataResult
import com.example.lexiapp.data.model.Game
import com.example.lexiapp.data.model.LetsReadGameDataResult
import com.example.lexiapp.data.model.WhereIsTheLetterDataResult
import com.example.lexiapp.data.model.toCorrectWordGameResult
import com.example.lexiapp.data.model.toWhereIsTheLetterResult
import com.example.lexiapp.domain.exceptions.FirestoreException
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Professional
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.*
import com.example.lexiapp.domain.service.FireStoreService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class FireStoreServiceImpl @Inject constructor(firebase: FirebaseClient) : FireStoreService {

    private val userCollection = firebase.firestore.collection("user")
    private val whereIsTheLetterCollection =
        firebase.firestore.collection(Game.WHERE_IS_THE_LETTER.toString().lowercase())
    private val correctWordCollection = firebase.firestore.collection(Game.CORRECT_WORD.toString().lowercase())
    private val letsReadCollection = firebase.firestore.collection(Game.LETS_READ.toString().lowercase())
    private val openaiCollection = firebase.firestore.collection("openai_api_use")
    private val professionalCollection = firebase.firestore.collection("professional")
    private val resultGameCollection: (String, String) -> CollectionReference =
        { collection: String, email: String -> firebase.firestore.collection("${collection}/${email}/results") }
    private val notesDocument = firebase.firestore.collection("profesional_notes")
    private val notesCollection: (String) -> CollectionReference =
        {email: String -> firebase.firestore.collection("profesional_notes/${email}/notes")}

    private val db = firebase.firestore
    private val categoryCollection = firebase.firestore.collection("category")

    override suspend fun saveAccount(user: User) {
        val data = hashMapOf(
            "user_name" to user.userName,
            "birth_date" to user.birthDate,
            "professional_link" to user.profesional as String?
        )
        userCollection.document(user.email).set(data).await()
    }

    override suspend fun getUser(email: String): User {
        val user = User(null, email, null, null)
        userCollection.document(email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot.exists()) {
                        user.userName = documentSnapshot.data?.get("user_name") as String?
                        user.birthDate = documentSnapshot.data?.get("birth_date") as String?
                        user.profesional =
                            documentSnapshot.data?.get("professional_link") as String?
                    } else {
                        // El usuario no fue encontrado
                    }
                } else {
                    // Hubo un error al obtener la información del usuario
                }
            }.await()
        return user
    }

    override suspend fun saveWhereIsTheLetterResult(
        result: WhereIsTheLetterDataResult,
        email: String
    ) {
        val data = hashMapOf(
            "result" to result.result,
            "mainLetter" to result.mainLetter,
            "selectedLetter" to result.selectedLetter,
            "word" to result.word
        )
        whereIsTheLetterCollection.document(email).collection("results")
            .document(System.currentTimeMillis().toString()).set(data).await()
    }

    override suspend fun getLastResultsWhereIsTheLetterGame(userMail: String) = flow {
        val result = mutableListOf<WhereIsTheLetterDataResult>()
        resultGameCollection(Game.WHERE_IS_THE_LETTER.toString().lowercase(), userMail).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val documentId = document.id
                    val data = document.data
                    result.add(
                        WhereIsTheLetterDataResult(
                            mainLetter = data["mainLetter"] as String,
                            result = data["result"] as Boolean,
                            selectedLetter = data["selectedLetter"] as String,
                            word = data["word"] as String,
                            date = documentId
                        )
                    )
                }
            }.await()
        emit(result.map { it.toWhereIsTheLetterResult(userMail) })
    }

    override suspend fun getLastResultsCorrectWordGame(userMail: String) = flow {
        val result = mutableListOf<CorrectWordDataResult>()
        resultGameCollection(Game.CORRECT_WORD.toString().lowercase(), userMail).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val documentId = document.id
                    val data = document.data
                    result.add(
                        CorrectWordDataResult(
                            mainWord = data["mainWord"] as String,
                            result = data["result"] as Boolean,
                            selectedWord = data["selectedWord"] as String,
                            date = documentId
                        )
                    )
                }
            }.await()
        emit(result.map { it.toCorrectWordGameResult(userMail) })
    }

    override suspend fun getOpenAICollectionDocumentReference(document: String) = flow {
        emit(openaiCollection.document(document))
    }

    override suspend fun saveProfessionalAccount(
        professional: Professional,
        registrationDate: Date
    ) {
        val data = hashMapOf(
            "user_name" to professional.user!!.userName,
            "medical_registration" to professional.medicalRegistration,
            "patients" to professional.patients,
            "is_verificated_account" to professional.isVerifiedAccount,
            "registration_date" to registrationDate
        )

        professionalCollection.document(professional.user.email)
            .set(data)
            .addOnSuccessListener {
                Log.v(TAG, "New professional saved in firestore")
            }
            .addOnFailureListener {
                throw FirestoreException("Failure to save a new professional")
            }
    }

    override suspend fun getProfessional(email: String): Professional {
        var professional = Professional(User(null, email), null, null, false, null)
        professionalCollection.document(email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot.exists()) {
                        professional = Professional.Builder()
                            .user(documentSnapshot.data?.get("user_name").toString(), email)
                            .medicalRegistration(
                                documentSnapshot.data?.get("medical_registration").toString()
                            )
                            .patients(documentSnapshot.data?.get("patients") as List<String>)
                            .isVerifiedAccount(documentSnapshot.data?.get("is_verificated_account") as Boolean)
                            .registrationDate((documentSnapshot.data?.get("registration_date") as Timestamp).toDate())
                            .build()
                    } else {
                        //User not found
                    }
                } else {
                    throw FirestoreException("Problema en firestore para obtener datos")
                }
            }.await()
        return professional
    }

    override suspend fun getIsLinked(email: String): Boolean? {
        var linkProfessional: Boolean? = null
        userCollection.document(email).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot.exists()) {
                        linkProfessional =
                            documentSnapshot.data?.get("professional_link") as String? != null
                        Log.v("FSSImpl_PRE_LINK_PROF", "$linkProfessional")
                    }
                } else {
                    throw FirestoreException("Problema en firestore para obtener datos")
                }
            }.await()
        Log.v("FSSImpl_POST_LINK_PROF", "$linkProfessional")
        return linkProfessional
    }

    override suspend fun bindProfessionalToPatient(
        emailPatient: String,
        emailProfessional: String
    ): FirebaseResult {
        var result: FirebaseResult = FirebaseResult.TaskFaliure
        val data = mapOf(
            "professional_link" to emailProfessional
        )
        userCollection.document(emailPatient).update(data)
            .addOnSuccessListener {
                result = FirebaseResult.TaskSuccess
            }
            .addOnFailureListener {
                throw FirestoreException("Failure to save a new professional")
            }.await()
        return result
    }

    override suspend fun addPatientToProfessional(
        emailPatient: String,
        emailProfessional: String
    ): CompletableDeferred<FirebaseResult> {
        val completableDeferred = CompletableDeferred<FirebaseResult>()
        db.runTransaction { transaction ->
            val documentSnapshot =
                transaction.get(professionalCollection.document(emailProfessional))
            val list = documentSnapshot.get("patients") as List<String>?
            val newList = list?.toMutableList() ?: mutableListOf()
            newList.add(emailPatient)
            transaction.update(
                professionalCollection.document(emailProfessional),
                "patients", newList
            )
        }.addOnSuccessListener {
            completableDeferred.complete(FirebaseResult.TaskSuccess)
        }.addOnFailureListener {
            completableDeferred.completeExceptionally(FirestoreException("Failure to save a new patient"))
        }
        return completableDeferred
    }

    override suspend fun getListLinkPatientOfProfessional(
        emailProfessional: String
    )= callbackFlow {
        val docRef = professionalCollection.document(emailProfessional)
        val listener = docRef.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val result = mutableListOf<String>()
            if (querySnapshot != null && querySnapshot.exists()) {
                val list: List<String> = querySnapshot.get("patients") as List<String>
                result.addAll(list)
            }
            trySend(result.toList()).isSuccess
        }
        awaitClose { listener.remove() }
    }

    override suspend fun unBindProfessionalFromPatient(
        emailPatient: String
    ): FirebaseResult {
        var result: FirebaseResult = FirebaseResult.TaskFaliure
        val data = mapOf(
            "professional_link" to null
        )
        userCollection.document(emailPatient).update(data)
            .addOnSuccessListener {
                result = FirebaseResult.TaskSuccess
            }
            .addOnFailureListener {
                throw FirestoreException("Failure to save a new professional")
            }.await()
        return result
    }

    override suspend fun deletePatientFromProfessional(
        emailPatient: String,
        emailProfessional: String
    ): CompletableDeferred<FirebaseResult> {
        val completableDeferred = CompletableDeferred<FirebaseResult>()
        db.runTransaction { transaction ->
            val documentSnapshot =
                transaction.get(professionalCollection.document(emailProfessional))
            val list = documentSnapshot.get("patients") as List<String>?
            val newList = list?.toMutableList() ?: mutableListOf()
            newList.remove(emailPatient)
            transaction.update(
                professionalCollection.document(emailProfessional),
                "patients", newList
            )
            newList
        }.addOnSuccessListener {
            //THIS LINE CLEAN ALL THE NOTES OF PATIENT, WHEN IT IS UNLINK
            cleanNotesFromPatient(emailPatient)
            completableDeferred.complete(FirebaseResult.TaskSuccess)
        }.addOnFailureListener {
            completableDeferred.completeExceptionally(FirestoreException("Failure to save a new patient"))
        }
        return completableDeferred
    }

    override suspend fun saveCorrectWordResult(result: CorrectWordDataResult, email: String) {
        val data = hashMapOf(
            "result" to result.result,
            "mainWord" to result.mainWord,
            "selectedWord" to result.selectedWord
        )
        correctWordCollection.document(email).collection("results")
            .document(System.currentTimeMillis().toString()).set(data).await()
    }

    override suspend fun saveObjectives(email: String, objectives: List<Objective>) {
        val firestore = FirebaseFirestore.getInstance()
        val collection = firestore.collection("objectives")
        val document = collection.document(email)
        val objectiveMap = hashMapOf<String, Any?>()

        objectives.forEachIndexed { index, objective ->
            val objectiveFields = hashMapOf<String, Any?>(
                "id" to objective.id,
                "title" to objective.title,
                "description" to objective.description,
                "progress" to objective.progress,
                "goal" to objective.goal
            )
            objectiveMap["objective$index"] = objectiveFields
        }

        document.set(objectiveMap)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
            }
            .await()
    }


    override suspend fun checkObjectivesExist(email: String): Boolean {
        val firestore = FirebaseFirestore.getInstance()
        val collection = firestore.collection("objectives")
        val document = collection.document(email)

        return try {
            val snapshot = document.get().await()
            snapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getObjectives(email: String): List<Objective> {
        val firestore = FirebaseFirestore.getInstance()
        val collection = firestore.collection("objectives")
        val document = collection.document(email)
        return try {
            val snapshot = document.get().await()
            if (snapshot.exists()) {
                val objectives = mutableListOf<Objective>()
                val objectiveMap = snapshot.data
                objectiveMap?.forEach { (_, objectiveFields) ->
                    if (objectiveFields is Map<*, *>) {
                        val id = objectiveFields["id"] as Long?
                        val title = objectiveFields["title"] as String?
                        val description = objectiveFields["description"] as String?
                        val progress = (objectiveFields["progress"] as Long?)?.toInt() ?: 0
                        val goal = (objectiveFields["goal"] as Long?)?.toInt()
                        Log.d(TAG, title.toString())
                        objectives.add(Objective(id, title, description, 0, goal))
                    }
                }
                objectives
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun saveLetsReadResult(result: LetsReadGameDataResult) {
        val data = hashMapOf(
            "wrongWords" to result.wrongWords,
            "totalWords" to result.totalWords,
            "success" to result.success
        )
        letsReadCollection.document(result.email).collection("results")
            .document(System.currentTimeMillis().toString()).set(data).await()
    }

    override suspend fun saveCategoriesFromPatient(email: String, categories: List<String>) {
        val data = hashMapOf(
            "categories" to categories
        )

        categoryCollection
            .document(email)
            .set(data)
            .addOnSuccessListener {
                Log.v(TAG, "Categories saved for patient $email")
            }
            .addOnFailureListener {
                throw FirestoreException("Failure to save categories for patient $email")
            }
    }

    override suspend fun getPatientCategories(email: String): List<String> {

        return suspendCoroutine { continuation ->
            categoryCollection
                .document(email)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        val categories = documentSnapshot.data?.get("categories") as List<String>
                        continuation.resume(categories)
                    } else {
                        continuation.resume(emptyList())
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    override suspend fun saveNote(note: Note) = flow {
        try {
            val data = hashMapOf(
                "note" to note.text
            )
            val documentRef = notesDocument.document(note.emailPatient)
                .collection("notes")
                .document(System.currentTimeMillis().toString())
            documentRef.set(data).await()
            emit(FirebaseResult.TaskSuccess)
        } catch (e: Exception) {
            emit(FirebaseResult.TaskFaliure)
        }
    }

    override suspend fun deleteNote (emailPatient: String, date: String) = flow {
        try {
            val notes = notesCollection(emailPatient).document(date).delete().await()
            emit(FirebaseResult.TaskSuccess)
        } catch (e: Exception) {
            emit(FirebaseResult.TaskFaliure)
        }
    }

    override suspend fun getNotes(emailPatient: String) = callbackFlow {
        val notesCollectionRef = notesDocument.document(emailPatient).collection("notes")
        val listener = notesCollectionRef.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val result = mutableListOf<Note>()
            for (document in querySnapshot!!.documents) {
                val date = document.id
                val data = document.data
                result.add(
                    Note(
                        text = data?.get("note")!! as String,
                        emailPatient = emailPatient,
                        date = date
                    )
                )
            }
            trySend(result.toList()).isSuccess
        }

        awaitClose { listener.remove() }
    }

    private fun cleanNotesFromPatient(emailPatient: String){
        notesCollection(emailPatient).get().addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot.documents) {
                document.reference.delete()
            }
        }
    }

    companion object {
        private const val TAG = "FireStoreServiceImpl"
    }
}
