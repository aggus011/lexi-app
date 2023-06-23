package com.example.lexiapp.domain.model

data class ProfessionalSignUp(
    val name: String,
    val surname: String,
    val medicalRegistration: String,
    val email: String,
    val emailConfirm: String,
    val password: String,
    val passwordConfirm: String,
    val showErrorDialog: Boolean = false
): UserType {
    fun mapToProfessional(): Professional{
        val user = User(name.plus(" $surname"), email)
        return Professional.Builder()
            .user(user.userName!!, user.email)
            .medicalRegistration(medicalRegistration)
            .patients(emptyList())
            .isVerifiedAccount(false)
            .registrationDate(null)
            .build()
    }


}
