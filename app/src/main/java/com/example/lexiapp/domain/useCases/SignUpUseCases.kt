package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.AuthenticationService
import com.example.lexiapp.domain.model.UserSignUp
import javax.inject.Inject

class SignUpUseCases @Inject constructor(
    private val authenticationService: AuthenticationService,
    private val profileUseCases: ProfileUseCases
) {

    suspend operator fun invoke(user: UserSignUp): Boolean {
        val validate = authenticationService.createAccount(user.email, user.password) != null
        if (validate) profileUseCases.saveProfile(user)
        return validate
    }

    fun validateInputs(name: String, email: String, emailSecond: String, password: String, passwordSecond: String): Boolean{
        if (name.isEmpty()) return false
        if (email.isEmpty()||emailSecond.isEmpty()||!email.contains("@") || email != emailSecond) return false
        if (password.isEmpty()||passwordSecond.isEmpty()||password!=passwordSecond) return false
        return true
    }
}