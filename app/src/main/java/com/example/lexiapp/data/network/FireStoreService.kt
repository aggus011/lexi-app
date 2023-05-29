package com.example.lexiapp.data.network

import android.util.Log
import com.example.lexiapp.domain.model.User
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await
import okhttp3.internal.concurrent.Task
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireStoreService @Inject constructor(firebase: FirebaseClient) {

    private val userCollection = firebase.firestore.collection("user")

    suspend fun saveAccount(user: User) {
        val data=hashMapOf(
            "user_name" to user.userName,
            "birth_date" to user.birthDate,
            "uri_image" to user.uri
            /*"birth_day" to birthDate.dayOfMonth,
            "birth_mounth" to birthDate.monthValue,
            "birth_year" to birthDate.year*/
        )
        userCollection.document(user.email).set(data).await()
    }

    /*fun getUserInfo(email: String) {
        //var user: User? = null
        firebase.firestore.collection("user").document(email).get().addOnSuccessListener {
            val userName= it.get("user_name") as String
            /*val birthDate = LocalDate.of(
                it.get("birth_day") as Int,
                it.get("birth_mounth") as Int,
                it.get("birth_year") as Int
            )*/
            //user= User(email, userName, null)
        }
        //return user
    }*/

    suspend fun getUser(email: String): User {
        val user =User(null, email, null, null)
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
}