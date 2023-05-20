package com.example.lexiapp.utils

sealed class FirebaseResult {
    object TaskSuccess : FirebaseResult()
    object TaskFaliure : FirebaseResult()
    object NoAction : FirebaseResult()
}