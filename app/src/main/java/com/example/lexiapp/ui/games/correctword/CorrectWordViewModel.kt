package com.example.lexiapp.ui.games.correctword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.model.gameResult.CorrectWordGameResult
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.useCases.CorrectWordUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CorrectWordViewModel @Inject constructor(
    private val useCases: CorrectWordUseCases,
    private val fireStoreService: FireStoreService
) : ViewModel() {

    init {
        generateWords()
    }

    var correctWord = ""

    private var _basicWords = MutableLiveData<List<String>>()
    var basicWords = _basicWords as LiveData<List<String>>

    fun generateWords() {
        viewModelScope.launch(Dispatchers.IO) {
            useCases.getWords()
                .collect {
                    withContext(Dispatchers.Main) {
                        _basicWords.value = it
                        correctWord = it[0]
                    }
                }
        }
    }

    fun validateAnswer(selecterWord: String): Boolean {
        val success = selecterWord == correctWord
        viewModelScope.launch {
            useCases.saveAnswer(
                CorrectWordGameResult(
                    email = "",
                    wordSelected = selecterWord,
                    correctWord = correctWord,
                    success = success,
                    date = null
                )
            )
            withContext(Dispatchers.IO) {
                if (success) {
                    fireStoreService.updateObjectiveProgress("CW", "hit")
                    fireStoreService.updateObjectiveProgress("CW", "play")
                } else {
                    fireStoreService.updateObjectiveProgress("CW", "play")
                }
            }
        }
        return success
    }
}

