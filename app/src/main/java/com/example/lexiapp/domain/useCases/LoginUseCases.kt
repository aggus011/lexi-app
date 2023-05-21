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

    /*
    fun singUpWithEmail(email: String, password: String): Task<AuthResult> {
        if (!verifyEmail(email)){
            throw SignUpException.EmailException
        }
        if(!verifyPassword(password)) {
            throw SignUpException.PasswordException
        }
        return authenticationService.createUserWithEmailAndPassword(email, password)
    }

    fun loginEmail(email: String, password: String): Task<AuthResult> {
        if(!verifyEmail(email)){
            throw SignUpException.EmailException
        }
        return authenticationService.login(email, password)
    }

    private fun verifyEmail(email: String): Boolean =
        PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()

    private fun verifyPassword(pass: String): Boolean = pass.length >= 6


    fun googleLogin(account: GoogleSignInAccount): Task<AuthResult> {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return mAuth.signInWithCredential(credential)

    }

    fun getUid(): String? {

        return if (mAuth.currentUser != null)
            mAuth.currentUser?.uid
        else
            null
    }

    fun getEmail(): String? {

        if (mAuth.currentUser != null)
            return mAuth.currentUser?.email
        else
            return null

    }

    fun singOut() {
        mAuth.signOut()
    }

     */
    companion object {
        private const val PASSWORD_MIN_LENGHT = 6
    }
}


