package com.example.lexiapp.domain.model

import java.util.*

class Professional(
    val user: User?,
    val medicalRegistration: String?,
    val patients: List<String>?,
    val isVerifiedAccount: Boolean = false,
    val registrationDate: Date?
) {
    data class Builder(
        var user: User? = null,
        var medicalRegistration: String? = null,
        var patients: List<String>? = null,
        var isVerifiedAccount: Boolean = false,
        var registrationDate: Date? = null
    ){
        fun user(userName: String, email: String) = apply{ this.user = User(userName, email) }
        fun medicalRegistration(medicalRegistration: String) = apply { this.medicalRegistration = medicalRegistration }
        fun patients(patients: List<String>) = apply { this.patients = patients }
        fun isVerifiedAccount(isVerifiedAccount: Boolean) = apply { this.isVerifiedAccount = isVerifiedAccount }
        fun registrationDate(registrationDate: Date?) = apply { this.registrationDate = registrationDate }
        fun build() = Professional(user, medicalRegistration, patients, isVerifiedAccount, registrationDate)
    }
}