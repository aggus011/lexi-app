package com.example.lexiapp.domain.model

sealed class LoginResult {
    object Error : LoginResult()
    object Success : LoginResult()
}