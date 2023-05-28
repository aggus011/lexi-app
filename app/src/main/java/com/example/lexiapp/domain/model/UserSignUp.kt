package com.example.lexiapp.domain.model

data class UserSignUp(
    val name: String,
    val email: String,
    val emailConfirm: String,
    val password: String,
    val passwordConfirm: String,
    val showErrorDialog: Boolean = false
) {
    fun mapToUser(): User {
        return User(name, email, null, null)
    }
}