package com.example.lexiapp.data.api

import android.util.Log
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class LetterRepositoryImpl @Inject constructor(
    private val apiWordService : WordAssociationService
) {

    suspend fun getWord(count: Int, length: Int, language: String)=flow {
        apiWordService.getWordToWhereIsTheLetterGame(count, length, language).collect{
            emit(it.uppercase())
            Log.v("data_in_repository","response word: $it")
        }
    }

}