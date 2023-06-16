package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.data.model.WhereIsGameResult
import com.example.lexiapp.data.model.toWhereIsTheLetterResult
import com.example.lexiapp.domain.exceptions.FirestoreException
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Professional
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.WhereIsTheLetterResult
import com.example.lexiapp.domain.service.FireStoreService
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireStoreServiceImpl @Inject constructor(firebase: FirebaseClient) : FireStoreService {

    private val userCollection = firebase.firestore.collection("user")
    private val whereIsTheLetterCollection = firebase.firestore.collection("where_is_the_letter")
    private val openaiCollection = firebase.firestore.collection("openai_api_use")
    private val professionalCollection = firebase.firestore.collection("professional")
    private val resultGameCollection: (String) -> CollectionReference =
        { email: String -> firebase.firestore.collection("where_is_the_letter/${email}/results") }
    private val db = firebase.firestore
    private lateinit var registration: ListenerRegistration

    override suspend fun saveAccount(user: User) {
        val data = hashMapOf(
            "user_name" to user.userName,
            "birth_date" to user.birthDate,
            "professional_link" to user.profesional
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

    override suspend fun saveWhereIsTheLetterResult(result: WhereIsGameResult, email: String) {
        val data = hashMapOf(
            "result" to result.result,
            "mainLetter" to result.mainLetter,
            "selectedLetter" to result.selectedLetter,
            "word" to result.word
        )
        whereIsTheLetterCollection.document(email).collection("results")
            .document(System.currentTimeMillis().toString()).set(data).await()
    }

    override suspend fun getLastResultsWhereIsTheLetterGame(userMail: String): List<WhereIsTheLetterResult> {
        val result = mutableListOf<WhereIsGameResult>()
        resultGameCollection(userMail).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val documentId = document.id
                    val data = document.data
                    result.add(
                        WhereIsGameResult(
                            mainLetter = data["mainLetter"] as String,
                            result = data["result"] as Boolean,
                            selectedLetter = data["selectedLetter"] as String,
                            word = data["word"] as String
                        )
                    )
                }
            }.await()
        return result.map { it.toWhereIsTheLetterResult(userMail) }
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
        val data = hashMapOf(
            "professional_link" to emailProfessional
        )
        userCollection.document(emailPatient).set(data)
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
        emailProfessional: String,
        listener: (List<String>?) -> Unit
    ) {
        val docRef = professionalCollection.document(emailProfessional)
        val registration = docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                listener(null)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val list = snapshot.get("patients") as List<String>?
                listener(list)
            } else {
                listener(null)
            }
        }
        this.registration = registration
    }

    override suspend fun unBindProfessionalFromPatient(
        emailPatient: String
    ): FirebaseResult {
        var result: FirebaseResult = FirebaseResult.TaskFaliure
        val data = hashMapOf(
            "professional_link" to null
        )
        userCollection.document(emailPatient).set(data)
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
            completableDeferred.complete(FirebaseResult.TaskSuccess)
        }.addOnFailureListener {
            completableDeferred.completeExceptionally(FirestoreException("Failure to save a new patient"))
        }
        return completableDeferred
    }

    companion object {
        private const val TAG = "FireStoreServiceImpl"
    }
}
