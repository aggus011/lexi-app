package com.example.lexiapp.domain.model

import java.util.*

data class User(
    var userName: String? = null,
    val email: String,
    var birthDate: String? = null,
    var profesional: String? = null
) : UserType {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (userName != other.userName) return false
        if (email != other.email) return false
        if (profesional != other.profesional) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userName?.hashCode() ?: 0
        result = 31 * result + email.hashCode()
        result = 31 * result + (profesional?.hashCode() ?: 0)
        return result
    }
}

