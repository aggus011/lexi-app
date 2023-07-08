package com.example.lexiapp.domain.useCases

import androidx.core.util.PatternsCompat
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.service.AuthenticationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class LoginUseCases @Inject constructor(
    private val authenticationServiceImpl: AuthenticationService
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        if (!verifyEmail(email)) {
            return LoginResult.Error
        }
        if(!verifyPassword(password)) {
            return LoginResult.Error
        }
        return authenticationServiceImpl.login(email, password)
    }

    private fun verifyEmail(email: String): Boolean =
        PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

    private fun verifyPassword(pass: String): Boolean = pass.length >= PASSWORD_MIN_LENGTH

    suspend fun sendRecoverEmail(email: String?): Flow<FirebaseResult?> {
        if(email==null) return flowOf(null)
        return authenticationServiceImpl.sendRecoverEmail(email)
    }


    companion object {
        private const val PASSWORD_MIN_LENGTH = 6
    }
}


