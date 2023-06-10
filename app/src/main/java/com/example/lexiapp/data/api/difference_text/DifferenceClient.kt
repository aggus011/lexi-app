package com.example.lexiapp.data.api.difference_text

import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.data.api.difference_text.model.SendInformation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface DifferenceClient {

    @POST("text?output_type=json&email=lexiapp.2023@gmail.com")
    suspend fun getDifference(
        @Header("Content-Type") contentType: String = "application/json",
        @Body SendInformation: SendInformation
    ): Response<Rows>
}