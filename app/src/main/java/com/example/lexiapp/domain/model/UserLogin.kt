package com.example.lexiapp.domain.model

data class UserLogin(
    val email: String = "",
    val password: String = "",
    val showErrorDialog: Boolean = false
)