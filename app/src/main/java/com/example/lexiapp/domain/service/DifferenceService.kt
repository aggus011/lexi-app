package com.example.lexiapp.domain.service

import com.example.lexiapp.data.api.difference_text.model.Rows
import retrofit2.Response

interface DifferenceService {

    suspend fun getDifference(originalText: String, revisedText: String) : Response<Rows>
}
