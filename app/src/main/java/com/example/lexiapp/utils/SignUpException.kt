package com.example.lexiapp.utils

sealed class SignUpException : Exception() {
    object EmailException: SignUpException()
    object PasswordException: SignUpException()
}
