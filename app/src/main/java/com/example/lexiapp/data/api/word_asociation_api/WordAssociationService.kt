package com.example.lexiapp.data.api.word_asociation_api

import android.util.Log
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class WordAssociationService @Inject constructor(
    private val client: WordAssociationClient
) {
    suspend fun getWordToWhereIsTheLetterGame(count: Int, length: Int, language: String )=flow {
        try{
            val response =client.getWordToWhereIsTheLetterGame(
                count=count,
                length=length,
                language=language)
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
