package com.example.lexiapp.domain

import android.content.ContentValues.TAG
import android.util.Log
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.flow
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class RepositoryImpl: Repository {

    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().build()
    private val stringJsonAdapter = moshi.adapter(Message::class.java)

    override fun getWordToLetterGame() = flow {
        val request = Request.Builder()
            .url("https://clientes.api.greenborn.com.ar/public-random-word")
            .build()
        var word = ""
        do {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                Log.d(TAG, response.body.toString())
                val body = stringJsonAdapter.fromJson(response.body!!.source()) ?: Message("Test")
                word = body.word
            }
        } while (word.length !in (1..6))
        emit(word)
    }

    class Message(
        val word: String
    )

}