package com.example.lexiapp.data.network

import com.example.lexiapp.domain.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireStoreService @Inject constructor(private val firebase: FirebaseClient) {

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

    suspend fun getUser(email: String): User? {
        val querySnapshot = userCollection.whereEqualTo("email", email).get().await()
        return querySnapshot.documents.firstOrNull()?.toObject(User::class.java)
    }
}