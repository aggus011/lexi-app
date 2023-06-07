package com.example.lexiapp.domain.model

import java.util.*

data class User(
    var userName: String?=null, val email: String, var birthDate: String?=null, var uri: String?=null
) : UserType
