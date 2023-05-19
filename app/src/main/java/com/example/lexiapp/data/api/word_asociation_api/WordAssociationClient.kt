package com.example.lexiapp.data.api.word_asociation_api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WordAssociationClient {
    @GET("word")
    suspend fun getWordToWhereIsTheLetterGame(
        @Query("number") count: Int = 1,
        @Query("length") maxLength: Int,
        @Query("lang") language: String = "es"
    ): Response<List<String>>

}