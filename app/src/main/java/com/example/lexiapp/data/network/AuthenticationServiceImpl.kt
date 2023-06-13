package com.example.lexiapp.data.network

import android.util.Log
import com.canhub.cropper.CropImage.CancelledResult.isSuccessful
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.service.AuthenticationService
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthenticationServiceImpl @Inject constructor(private val firebase: FirebaseClient):
    AuthenticationService {

    override suspend fun login(email: String, password: String): LoginResult = runCatching {
        firebase.auth.signInWithEmailAndPassword(email, password).await()
    }.toLoginResult()

    override suspend fun createAccount(email: String, password: String): LoginResult = runCatching {
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

    override fun signOut() {
        firebase.auth.signOut()
    }

    override suspend fun sendRecoverEmail(email: String) = firebase.auth.sendPasswordResetEmail(email)
}