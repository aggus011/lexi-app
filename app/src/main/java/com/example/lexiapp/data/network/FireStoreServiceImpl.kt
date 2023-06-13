package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.data.model.Game
import com.example.lexiapp.data.model.GameResult
import com.example.lexiapp.data.model.WhereIsGameResult
import com.example.lexiapp.domain.exceptions.FirestoreException
import com.example.lexiapp.domain.exceptions.UserNotFoundException
import com.example.lexiapp.domain.model.Professional
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.service.FireStoreService
import com.google.firebase.Timestamp
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

    override suspend fun saveAccount(user: User) {
        val data = hashMapOf(
            "user_name" to user.userName,
            "birth_date" to user.birthDate,
            "uri_image" to user.uri
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
                        user.userName = documentSnapshot.data?.get("user_name").toString()
                        Log.v("USER_NAME_FIRESTORE_SERVICE", "${user.userName} // ${user.email}")
                        user.uri = documentSnapshot.data?.get("uri_image").toString()
                    } else {
                        // El usuario no fue encontrado
                    }
                } else {
                    // Hubo un error al obtener la información del usuario
                }
            }.await()
        return user
    }

    override suspend fun saveWhereIsTheLetterResult(result: GameResult) {
        val data = hashMapOf(
            "game" to result.game.toString(),
            "result" to result.result
        )
        whereIsTheLetterCollection.document(result.user_mail).set(data).await()
    }

    override suspend fun obtainLastResults(userMail: String): List<WhereIsGameResult> {
        var result = mutableListOf<WhereIsGameResult>()
        whereIsTheLetterCollection.document(userMail).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    documentSnapshot.let {
                        if (it.data?.get("game")
                                .toString() == Game.WHERE_IS_THE_LETTER_GAME.toString()
                        ) {
                            result.add(
                                WhereIsGameResult(
                                    game = Game.valueOf(it.data?.get("game") as String),
                                    user_mail = userMail,
                                    result = it.data?.get("result") as Pair<String, String>
                                )
                            )
                        }
                    }
                }
            }.await()
        return result
    }

    override suspend fun getOpenAICollectionDocumentReference(document: String) = flow {
        emit(openaiCollection.document(document))
    }

    override suspend fun saveProfessionalAccount(professional: Professional, registrationDate: Date) {
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
                            .medicalRegistration(documentSnapshot.data?.get("medical_registration").toString())
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

    companion object{
        private const val TAG = "FireStoreServiceImpl"
    }
}
