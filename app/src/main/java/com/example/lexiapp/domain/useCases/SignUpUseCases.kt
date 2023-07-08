package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import androidx.core.util.PatternsCompat
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.service.AuthenticationService
import com.example.lexiapp.domain.service.FireStoreService
import javax.inject.Inject

class SignUpUseCases @Inject constructor(
    private val authenticationServiceImpl: AuthenticationService,
    private val fireStoreServiceImpl: FireStoreService,
    private val sharedPrefs: SharedPreferences
) {
    private val editor = sharedPrefs.edit()

    suspend operator fun invoke(user: UserSignUp): LoginResult {
        if (!verifyEmail(user.email) || user.email != user.emailConfirm) {
            return LoginResult.Error
        }
        if (!verifyPassword(user.password) || user.password != user.passwordConfirm) {
            return LoginResult.Error
        }
        return authenticationServiceImpl.createAccount(user.email, user.password)
    }

    private fun verifyEmail(email: String): Boolean {
        return if (email.isBlank())
            false
        else
            PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun verifyPassword(pass: String): Boolean =
        pass.length >= PASSWORD_MIN_LENGTH

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

    suspend fun savePatientAccount(user: UserSignUp) {
        fireStoreServiceImpl.saveAccount(user.mapToUser())
        editor.putString("email", user.email).apply()
        editor.putString("user_type", "patient").apply()
        editor.putString("user_name", user.name).apply()
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 6
    }
}