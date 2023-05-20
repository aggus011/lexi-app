package com.example.lexiapp.data.word_asociation_api

import android.util.Log
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class WordAssociationClient(
    private val service: WordAssociationService
) {
    suspend fun getWordToWhereIsTheLetterGame()=flow {
        try{
            val response =service.getWordToWhereIsTheLetterGame(maxLength = Random.nextInt(2, 9))
            val word = if (response.isSuccessful && response.body()!=null) response.body()!![0] else stimulus()
            Log.v("", "${response.code()}${response.body()?.get(0)}")
            emit(word)
        }catch (e: Exception){
            Log.v("EXCEPTION", "${e.message}")
            emit(stimulus())
        }
    }

    private fun stimulus()=listOf("Esfínter", "Sintomático", "Sinestesia", "Austeridad", "Psicodélico", "Epistemología",
            "Atemporal", "Ecléctico", "Abigarrado", "Vértigo", "Heterogéneo", "Sincrónico",
            "Catártico", "Endémico", "Anodino", "Perplejidad").random().toString()
}
