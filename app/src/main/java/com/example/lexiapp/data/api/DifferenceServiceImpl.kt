package com.example.lexiapp.data.api

import com.example.lexiapp.data.api.difference_text.DifferenceGateway
import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.data.api.difference_text.model.SendInformation
import com.example.lexiapp.domain.service.DifferenceService
import retrofit2.Response
import javax.inject.Inject

class DifferenceServiceImpl @Inject constructor(
    private val apiDifferenceGateway: DifferenceGateway
) : DifferenceService {

    override suspend fun getDifference(originalText: String, revisedText: String) : Response<Rows> {
        val sendInformation = SendInformation(originalText, revisedText)
        return apiDifferenceGateway.getDifference(sendInformation)
    }
}