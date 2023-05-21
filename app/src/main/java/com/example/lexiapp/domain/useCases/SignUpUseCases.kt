package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.AuthenticationService
import com.example.lexiapp.data.response.LoginResult
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class SignUpUseCases @Inject constructor(
    private val authenticationService: AuthenticationService
) {

    suspend operator fun invoke(email: String, password: String): Boolean {
        return authenticationService.createAccount(email, password) != null
    }

}