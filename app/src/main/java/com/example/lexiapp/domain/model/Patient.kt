package com.example.lexiapp.domain.model

class Patient (
    val email: String?,
    val name: String?
    ){
    data class Builder(
        var email: String? = null,
        var name: String? = null){

        fun email(email: String?) = apply { this.email = email }
        fun name(name: String?) = apply { this.name = name }
        fun build() = Patient(email, name)
    }
}
