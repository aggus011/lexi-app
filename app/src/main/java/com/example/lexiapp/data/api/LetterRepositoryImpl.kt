package com.example.lexiapp.data.api

import android.util.Log
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LetterRepositoryImpl @Inject constructor(
    private val apiWordClient : WordAssociationClient
): LetterRepository {

    override suspend fun getWord()=flow {
        var word=""
        do {
            apiWordClient.getWordToWhereIsTheLetterGame().collect{
                word=it.uppercase()
                Log.v("data_in_repository","response word: $word")
            }
        } while (word.length !in (1..8))
        emit(word)
    }
}