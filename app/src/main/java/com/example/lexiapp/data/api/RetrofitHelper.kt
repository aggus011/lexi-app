package com.example.lexiapp.data.api

import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

object RetrofitHelper  {

    fun getRetrofit(): Retrofit {
        Retrofit.Builder()
            .baseUrl("https://api.wordassociations.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordAssociationService::class.java)
    }
    }
}