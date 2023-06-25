package com.example.lexiapp.domain.model

sealed class FirebaseResult {
    object TaskSuccess : FirebaseResult()
    object TaskFailure : FirebaseResult()
    object EmailError : FirebaseResult()
    object PasswordError: FirebaseResult()
}