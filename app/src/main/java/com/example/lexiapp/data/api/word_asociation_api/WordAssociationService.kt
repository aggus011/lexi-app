package com.example.lexiapp.data.api.word_asociation_api

import android.util.Log
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.random.Random

class WordAssociationService @Inject constructor(
    private val client: WordAssociationClient
) {
    suspend fun getWordToWhereIsTheLetterGame(count: Int, length: Int, language: String, estimulo: List<String?>) = flow {
        Log.d("Estimulo", estimulo.toString())
        try {
            var text = ""
            if(estimulo.isNotEmpty()){
                estimulo.forEach { text += "$it " }
            } else {
                stimulus().forEach { text += "$it " }
            }
            val response = client.getWordToWhereIsTheLetterGame(
                count = count + 10,
                language = language,
                text = text
            )
            Log.d("Api response", response.body().toString())
            val word = mutableListOf<String>()
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.wordsAsociate.map {
                    it.blocks.forEach { item ->
                        word.add(item.word)
                    }
                }
            } else word.addAll(stimulus())
            Log.v("API Response", "${response.code()}${response.raw()}:: $word")
            emit(word.filter { it.length in 3..7 }
                .shuffled(Random(System.currentTimeMillis() % word.size)))
        } catch (e: NullPointerException) {
            Log.v("EXCEPTION", "${e.message}, NAME: $e")
        }
    }.catch {
        Log.d("EXCEPTION", it.cause.toString())
        emit(stimulus())
    }

    private fun stimulus() = listOf(
        "Azul", "Síntoma", "Perro", "Cosas", "Mano", "Cactus",
        "Arbusto", "Cielo", "Cortina", "Pelota", "Carrera", "Moto",
        "Horno", "Espejo", "Papel", "Carton"
    ).shuffled(Random(System.currentTimeMillis() % 16)).take(2)
}
