package com.example.lexiapp.data.model

import com.google.gson.annotations.SerializedName

data class NotificationRequest(
    @SerializedName("to")
    val token: String,

    @SerializedName("priority")
    val priority: String = "high",

    @SerializedName("notification")
    val notification: NotificationData
)

data class NotificationData(
    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String
)
