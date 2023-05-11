package com.example.lexiapp.domain

import com.example.lexiapp.data.database.dao.LetterGameDao
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.stream.IntStream.range

class RepositoryImpl(
    localSource: LetterGameDao
): Repository {

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().build()
    private val stringJsonAdapter = moshi.adapter(String::class.java)

    override fun getWordToLetterGame() = flow {
        val request = Request.Builder()
            .url("https://clientes.api.greenborn.com.ar/public-random-word")
            .build()
        var word = ""
        do {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                word = stringJsonAdapter.fromJson(response.body!!.source()) ?: ""
            }
        } while (word.length !in (1..6))
        emit(word)
    }

}