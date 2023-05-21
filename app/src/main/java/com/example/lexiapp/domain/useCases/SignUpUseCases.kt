package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.AuthenticationService
import com.example.lexiapp.data.response.LoginResult
import com.example.lexiapp.domain.model.UserSignUp
import com.google.firebase.auth.AuthResult
import javax.inject.Inject

class SignUpUseCases @Inject constructor(
    private val authenticationService: AuthenticationService
) {

    suspend operator fun invoke(user: UserSignUp): Boolean {
        return authenticationService.createAccount(user.email, user.password) != null
    }

}