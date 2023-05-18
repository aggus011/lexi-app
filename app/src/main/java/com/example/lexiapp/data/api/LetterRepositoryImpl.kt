package com.example.lexiapp.data.api

import android.util.Log
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationClient
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LetterRepositoryImpl @Inject constructor(
    private val apiWordClient : WordAssociationClient
): LetterRepository {

    override suspend fun getWord()=flow {
        apiWordClient.getWordToWhereIsTheLetterGame().collect{
            emit(it.uppercase())
            Log.v("data_in_repository","response word: $it")
        }
    }

}