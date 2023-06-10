package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.data.model.Game
import com.example.lexiapp.data.model.GameResult
import com.example.lexiapp.data.model.WhereIsGameResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.service.FireStoreService
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireStoreServiceImpl @Inject constructor(firebase: FirebaseClient) : FireStoreService {

    private val userCollection = firebase.firestore.collection("user")
    private val whereIsTheLetterCollection = firebase.firestore.collection("where_is_the_letter")
    private val openaiCollection = firebase.firestore.collection("openai_api_use")

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
}
