package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.api.DifferenceRepository
import com.example.lexiapp.data.api.difference_text.model.Rows
import retrofit2.Response
import javax.inject.Inject

class DifferenceUseCases @Inject constructor(
    private val repository: DifferenceRepository
){

    suspend fun getDifference(originalText: String, revisedText: String) : Response<Rows> =
        repository.getDifference(originalText, revisedText)

}