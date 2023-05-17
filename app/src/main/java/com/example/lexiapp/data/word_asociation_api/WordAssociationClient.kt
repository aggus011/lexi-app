package com.example.lexiapp.data.word_asociation_api

import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WordAssociationClient(
    private val service: WordAssociationService
) {
    private val KEY=""
    private val retrofit=Retrofit.Builder()
        .baseUrl("https://api.wordassociations.net")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    suspend fun getWordToWhereIsTheLetterGame()=flow {
        val response =service.getWordToWhereIsTheLetterGame(KEY,stimulus())
        if (response.isSuccessful) {
            val assciation = response.body()
            if (assciation != null) {
                emit(assciation.wordsAsociate[0].word)
            }
        } else {
            //Handle errors
        }
    }

    private fun stimulus()=listOf("Esfínter", "Paradójico", "Sintomático", "Sinestesia", "Austeridad", "Psicodélico", "Epistemología",
            "Atemporal", "Polifacético", "Ecléctico", "Ornitorrinco", "Abigarrado", "Vértigo", "Heterogéneo", "Sincrónico",
            "Catártico", "Endémico", "Anodino", "Perplejidad").random().toString()
}
