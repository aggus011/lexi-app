package com.example.lexiapp.data.api

import com.example.lexiapp.data.api.difference_text.DifferenceService
import com.example.lexiapp.data.api.difference_text.model.Rows
import com.example.lexiapp.data.api.difference_text.model.SendInformation
import retrofit2.Response
import javax.inject.Inject

class DifferenceRepository @Inject constructor(
    private val apiDifferenceService: DifferenceService
) {

    suspend fun getDifference(originalText: String, revisedText: String) : Response<Rows> {
        val sendInformation = SendInformation(originalText, revisedText)
        return apiDifferenceService.getDifference(sendInformation)
    }
}