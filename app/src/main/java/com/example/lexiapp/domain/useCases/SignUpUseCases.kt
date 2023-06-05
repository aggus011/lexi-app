package com.example.lexiapp.domain.useCases

import androidx.core.util.PatternsCompat
import com.example.lexiapp.data.network.AuthenticationService
import com.example.lexiapp.data.response.LoginResult
import com.example.lexiapp.domain.model.UserSignUp
import javax.inject.Inject

class SignUpUseCases @Inject constructor(
    private val authenticationService: AuthenticationService,
    //private val profileUseCases: ProfileUseCases
) {

    suspend operator fun invoke(user: UserSignUp): LoginResult {
        if (!verifyEmail(user.email) || user.email != user.emailConfirm) {
            return LoginResult.Error
        }
        if (!verifyPassword(user.password) || user.password != user.passwordConfirm) {
            return LoginResult.Error
        }
        return authenticationService.createAccount(user.email, user.password)
    }

    private fun verifyEmail(email: String): Boolean {
        return if (email.isBlank())
            false
        else
            PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun verifyPassword(pass: String): Boolean =
        pass.length >= PASSWORD_MIN_LENGHT

    fun validateInputs(
        name: String,
        email: String,
        emailSecond: String,
        password: String,
        passwordSecond: String
    ): Boolean {
        if (name.isEmpty()) return false
        if (email.isEmpty() || emailSecond.isEmpty() || !email.contains("@") || email != emailSecond) return false
        if (password.isEmpty() || passwordSecond.isEmpty() || password != passwordSecond) return false
        return true
    }

    companion object {
        private const val PASSWORD_MIN_LENGHT = 6
    }
}