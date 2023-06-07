package com.example.lexiapp.domain.model

data class UserSignUp(
    val name: String,
    val email: String,
    val emailConfirm: String,
    val password: String,
    val passwordConfirm: String,
    val showErrorDialog: Boolean = false
): UserType {
    fun mapToUser() = User(name, email, null, null)
}