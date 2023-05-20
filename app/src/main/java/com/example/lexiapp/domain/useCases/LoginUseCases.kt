package com.example.lexiapp.domain.useCases

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.example.lexiapp.utils.FirebaseResult
import com.example.lexiapp.utils.SignUpException
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCases @Inject constructor(
    private val mAuth: FirebaseAuth
) {

    fun singUpWithEmail(email: String, password: String): Task<AuthResult> {
        if (!verifyEmail(email)){
            throw SignUpException.EmailException
        }
        if(!verifyPassword(password)) {
            throw SignUpException.PasswordException
        }
        return mAuth.createUserWithEmailAndPassword(email, password)
    }

    fun loginEmail(email: String, password: String): Task<AuthResult> {
        if(!verifyEmail(email)){
            throw SignUpException.EmailException
        }
        return mAuth.signInWithEmailAndPassword(email, password)
    }

    private fun verifyEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()

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
}
