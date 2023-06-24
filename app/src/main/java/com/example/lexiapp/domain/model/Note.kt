package com.example.lexiapp.domain.model

data class Note (
    val text: String,
    val emailPatient: String,
    val date: String? = null
    )