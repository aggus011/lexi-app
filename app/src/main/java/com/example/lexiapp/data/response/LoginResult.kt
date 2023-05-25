package com.example.lexiapp.data.response

sealed class LoginResult {
    object Error : LoginResult()
    object Success : LoginResult()
}