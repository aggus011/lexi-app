package com.example.lexiapp.domain.useCases

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

class LoginUseCases @Inject constructor(private val mAuth: FirebaseAuth) {

    fun singUpWithEmail(email: String, password: String): Task<AuthResult> {
        return mAuth.createUserWithEmailAndPassword(email, password)
    }

    fun loginEmail(email: String, password: String): Task<AuthResult> {
        return mAuth.signInWithEmailAndPassword(email, password)
    }

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
