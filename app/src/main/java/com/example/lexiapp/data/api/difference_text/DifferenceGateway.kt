package com.example.lexiapp.data.api.difference_text

import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.data.api.difference_text.model.SendInformation
import retrofit2.Response
import javax.inject.Inject

class DifferenceGateway @Inject constructor(
    private val client: DifferenceClient
){
    suspend fun getDifference(sendInformation: SendInformation): Response<Rows> =
        client.getDifference(SendInformation = sendInformation)
}