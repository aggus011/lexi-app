package com.example.lexiapp.domain.model

import android.opengl.Visibility

class Patient (
    val user: User?,
    val visibility: Boolean = false,
    val results: String = ""
    ){
    data class Builder(
        var user: User? = null,
        var visibility: Boolean = false,
        //Change for results from firestore
        var results: String = ""){

        fun user(email: String, userName: String) = apply { this.user = User(userName = userName, email = email) }
        fun visibility(visibility: Boolean) = apply { this.visibility = visibility }
        fun result(results: String) = apply { this.results= results }
        fun build() = Patient(user, visibility, results)
    }
}
