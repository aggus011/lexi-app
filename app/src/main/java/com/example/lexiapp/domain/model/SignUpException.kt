package com.example.lexiapp.domain.model

sealed class SignUpException : Exception() {
    object EmailException: SignUpException()
    object PasswordException: SignUpException()
}
