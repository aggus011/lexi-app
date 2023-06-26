package com.example.lexiapp.data.network

import com.example.lexiapp.data.notification.NotificationRequest
import com.example.lexiapp.domain.model.Secrets
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseNotificationClient {

    @Headers(
        "Content-Type: application/json",
        "Authorization: key=${Secrets.FIREBASE_SERVER_KEY}"
    )
    @POST("fcm/send")
    suspend fun sendNotification(@Body notification: NotificationRequest): Response<ResponseBody>

}