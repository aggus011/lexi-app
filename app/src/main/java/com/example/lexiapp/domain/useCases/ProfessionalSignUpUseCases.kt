package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import android.util.Log
import androidx.core.util.PatternsCompat
import com.example.lexiapp.domain.exceptions.FirestoreException
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.model.ProfessionalSignUp
import com.example.lexiapp.domain.service.AuthenticationService
import com.example.lexiapp.domain.service.FireStoreService
import java.util.*
import javax.inject.Inject

class ProfessionalSignUpUseCases @Inject constructor(
    private val authenticationServiceImpl: AuthenticationService,
    private val firestoreServiceImpl: FireStoreService,
    private val sharedPrefs: SharedPreferences
) {
    private val editor = sharedPrefs.edit()

    suspend operator fun invoke(user: ProfessionalSignUp): LoginResult {
        if (!verifyEmail(user.email)) return LoginResult.EmailInvalid
        if(user.email != user.emailConfirm) return LoginResult.DistinctEmail
        if (!verifyPassword(user.password)) return LoginResult.PasswordInvalid
        if(user.password != user.passwordConfirm) return LoginResult.DistinctPassword
        return authenticationServiceImpl.createAccount(user.email, user.password)
    }

    private fun verifyEmail(email: String) =
        PatternsCompat.EMAIL_ADDRESS.matcher(email.trim()).matches()

    private fun verifyPassword(pass: String): Boolean =
        pass.length >= PASSWORD_MIN_LENGTH

    suspend fun saveProfessional(user: ProfessionalSignUp) {
        try {
            firestoreServiceImpl.saveProfessionalAccount(user.mapToProfessional(), Date())
            editor.putString("email", user.email).apply()
            editor.putInt("professional_account_state", 1).apply()
            editor.putString("user_type", "professional").apply()
            editor.putString("user_name", user.name.plus(" ${user.surname}")).apply()
        } catch (e: FirestoreException) {
            Log.v(TAG, "Error to save professional in db")
        }
    }

    fun getLastProfessionalStatePreference(): Int {
        return sharedPrefs.getInt("professional_account_state", 0)
    }

    companion object {
        private const val PASSWORD_MIN_LENGTH = 6
        private const val TAG = "ProfessionalSignUpUseCases"
    }

}