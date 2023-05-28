package com.example.lexiapp.domain.model

import java.text.SimpleDateFormat

data class User(
    var userName: String?=null, val email: String, var birthDate: SimpleDateFormat?=null, var uri: String?=null
) : UserType
