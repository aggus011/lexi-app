package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.AuthenticationService
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.data.network.FireStoreService
import com.example.lexiapp.domain.model.User
import javax.inject.Inject

class SignUpUseCases @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val fireStoreService: FireStoreService
) {

    suspend operator fun invoke(user: UserSignUp): Boolean {
        val validate = authenticationService.createAccount(user.email, user.password) != null
        if (validate) saveAccount(user)
        return validate
    }

    private suspend fun saveAccount(user: UserSignUp){
        fireStoreService.saveAccount(user.mapToUser())
    }

    suspend fun getUser(email: String): User {
        return fireStoreService.getUser(email)
    }

    fun validateInputs(name: String, email: String, emailSecond: String, password: String, passwordSecond: String): Boolean{
        if (name.isEmpty()) return false
        if (email.isEmpty()||emailSecond.isEmpty()||!email.contains("@") || email != emailSecond) return false
        if (password.isEmpty()||passwordSecond.isEmpty()||password!=passwordSecond) return false
        return true
    }
}