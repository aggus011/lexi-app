package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.LoginResult

interface AuthenticationService {

    suspend fun login(email: String, password: String): LoginResult

    suspend fun createAccount(email: String, password: String): LoginResult

    fun signOut()
}