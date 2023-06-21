package com.example.lexiapp.data.api.word_asociation_api

import android.util.Log
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class WordAssociationService @Inject constructor(
    private val client: WordAssociationClient
) {
    suspend fun getWordToWhereIsTheLetterGame(count: Int, length: Int, language: String) = flow {

        try {
            val response = client.getWordToWhereIsTheLetterGame(
                count = count,
                length = length,
                language = language
            )
            val word: List<String> =
                if (response.isSuccessful && response.body() != null) response.body()!! else stimulus()
            Log.v("rta", "${response.code()}${response.body()?.get(0)}:: $word")
            emit(word)
        } catch (e: NullPointerException) {
            Log.v("EXCEPTION", "${e.message}, NAME: $e")
        }
    }.catch {
        emit(stimulus())
    }

    private fun stimulus() = listOf(
        "Azul", "Síntoma", "Perro", "Cosas", "Mano", "Cactus",
        "Arbusto", "Cielo", "Cortina", "Pelota", "Carrera", "Moto",
        "Horno", "Espejo", "Papel", "Carton"
    ).shuffled(Random(System.currentTimeMillis() % 16)).take(3)
}
