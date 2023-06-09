package com.example.lexiapp.domain.model

class Patient (
    val user: User?,
    var expanded: Boolean = false,
    val results: String = ""
    ){
    data class Builder(
        var user: User? = null,
        var expanded: Boolean = false,
        //Change for results from firestore
        var results: String = "ESTAS SON LAS ESTADISTICAS"){

        fun user(email: String, userName: String) = apply { this.user = User(userName = userName, email = email) }
        fun expanded(expanded: Boolean) = apply { this.expanded = expanded }
        fun result(results: String) = apply { this.results= results }
        fun build() = Patient(user, expanded, results)
    }
}
