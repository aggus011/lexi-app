package com.example.lexiapp.data.word_asociation_api

import com.example.lexiapp.data.word_asociation_api.model.WordAssociationResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WordAssociationService {
    @GET("word")
    suspend fun getWordToWhereIsTheLetterGame(
        @Query("number") count: Int = 1,
        @Query("length") maxLength: Int,
        @Query("lang") language: String = "es"
    ): Response<List<String>>

    /*@GET("/associations/v1.0/json/search")
    suspend fun getWordToWhereIsTheLetterGame(
        @Query("text") text: String,
        @Query("lang") lang: String = "es",
        @Query("limit") limit: Int = 1
    ): Response<WordAssociationResponse>

    @GET("/associations/v1.0/json/search")
    suspend fun getWordToCorrectWordGame(
        @Query("apikey") apiKey: String,
        @Query("text") text: String,
        @Query("lang") lang: String = "es",
        @Query("limit") limit: Int = 4
    ): Flow<WordAssociationResponse>*/
}