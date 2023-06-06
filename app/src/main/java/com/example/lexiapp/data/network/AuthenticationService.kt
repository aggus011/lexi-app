package com.example.lexiapp.data.network

import com.example.lexiapp.data.response.LoginResult
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationService @Inject constructor(private val firebase: FirebaseClient) {

    suspend fun login(email: String, password: String): LoginResult = runCatching {
        firebase.auth.signInWithEmailAndPassword(email, password).await()
    }.toLoginResult()

    suspend fun createAccount(email: String, password: String): LoginResult = runCatching {
        firebase.auth.createUserWithEmailAndPassword(email, password).await()
    }.toLoginResult()

    private fun Result<AuthResult>.toLoginResult() = when (val result = getOrNull()) {
        null -> LoginResult.Error
        else -> {
            val userId = result.user
            checkNotNull(userId)
            LoginResult.Success
        }
    }

    fun signOut() {
        firebase.auth.signOut()
    }


}