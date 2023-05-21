package com.example.lexiapp.domain.model

data class UserSignUp(
    val name: String,
    val email: String,
    val password: String,
    val showErrorDialog: Boolean = false
)