package com.example.lexiapp.data.api

import android.util.Log
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import com.example.lexiapp.data.repository.BlackList
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class LetterRepositoryImpl @Inject constructor(
    private val apiWordService: WordAssociationService
) {

    suspend fun getWord(count: Int, length: Int, language: String) = flow {
        apiWordService.getWordToWhereIsTheLetterGame(count, length, language)
            .map { inputList -> inputList.filter { !BlackList.words.contains(it.uppercase()) } }
            .collect {
                emit(it[0].uppercase())
                Log.v("data_in_repository", "response word: $it")
            }
    }

}