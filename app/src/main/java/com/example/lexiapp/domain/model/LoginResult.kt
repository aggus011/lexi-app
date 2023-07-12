package com.example.lexiapp.domain.model

sealed class LoginResult {
    object Error : LoginResult()
    object Success : LoginResult()
    object DistinctEmail: LoginResult()
    object EmailInvalid: LoginResult()
    object PasswordInvalid: LoginResult()
    object DistinctPassword: LoginResult()
}