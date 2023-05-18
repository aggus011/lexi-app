package com.example.lexiapp.domain

import android.util.Log
import com.example.lexiapp.data.word_asociation_api.WordAssociationClient
import kotlinx.coroutines.flow.flow

class LetterRepository(
    private val apiWordClient : WordAssociationClient
) {

    suspend fun getWord()=flow {
        apiWordClient.getWordToWhereIsTheLetterGame().collect{
            emit(it.uppercase())
            Log.v("data_in_repository","response word: $it")
        }
    }
}