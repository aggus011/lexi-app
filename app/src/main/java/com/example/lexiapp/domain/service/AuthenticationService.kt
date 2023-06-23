package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.LoginResult
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface AuthenticationService {

    suspend fun login(email: String, password: String): LoginResult

    suspend fun createAccount(email: String, password: String): LoginResult

    fun signOut()

    suspend fun sendRecoverEmail(email: String): Flow<FirebaseResult>
}