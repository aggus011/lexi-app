package com.example.lexiapp.data.api.word_asociation_api

import com.example.lexiapp.data.api.word_asociation_api.model.WordAssociation
import com.example.lexiapp.domain.model.Secrets
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WordAssociationClient {
    @GET("search")
    suspend fun getWordToWhereIsTheLetterGame(
        @Query("limit") count: Int,
        @Query("text") text: String,
        @Query("lang") language: String,
        @Query("apikey") apiKey: String = Secrets.WORD_API_KEY
    ): Response<WordAssociation>

}