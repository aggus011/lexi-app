package com.example.lexiapp.domain.model

data class ProfessionalValidation(
    val name: String,
    val email: String,
    val medicalRegistration: String,
    val validated: Boolean
)