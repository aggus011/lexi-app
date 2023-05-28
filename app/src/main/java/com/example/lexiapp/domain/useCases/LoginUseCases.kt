package com.example.lexiapp.domain.useCases

import androidx.core.util.PatternsCompat
import com.example.lexiapp.data.network.AuthenticationService
import com.example.lexiapp.data.response.LoginResult
import javax.inject.Inject

class LoginUseCases @Inject constructor(
    private val authenticationService: AuthenticationService
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        if (!verifyEmail(email)) {
            return LoginResult.Error
        }
        if(!verifyPassword(password)) {
            return LoginResult.Error
        }
        return authenticationService.login(email, password)
    }

    fun signOut() {
        authenticationService.signOut()
    }

    private fun verifyEmail(email: String): Boolean =
        PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

    private fun verifyPassword(pass: String): Boolean = pass.length >= PASSWORD_MIN_LENGHT

    companion object {
        private const val PASSWORD_MIN_LENGHT = 6
    }
}


