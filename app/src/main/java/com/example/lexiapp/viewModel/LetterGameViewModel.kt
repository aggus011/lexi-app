package com.example.lexiapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Math.random

class LetterGameViewModel(val repository: Repository) : ViewModel() {

    private var _selectedPosition = MutableStateFlow<Int?>(null)
    val selectedPosition = _selectedPosition.asStateFlow()

    private var _basicWord = MutableStateFlow<String?>(null)
    val basicWord = _basicWord.asStateFlow()

    private var correctPosition = -1

    private var _correctAnswerSubmitted = MutableStateFlow(false)
    val correctAnswerSubmitted = _correctAnswerSubmitted.asStateFlow()

    private var _incorrectAnswerSubmitted = MutableStateFlow(false)
    val incorrectAnswerSubmitted = _incorrectAnswerSubmitted.asStateFlow()

    init {
        generateWord()
    }

    fun onPositionSelected(position: Int) {
        _selectedPosition.value = position
    }

    fun onOmitWord() {
        generateWord()
    }

    fun onSubmitAnswer() {
        if(selectedPosition.value == correctPosition) {
            _correctAnswerSubmitted.value = true
        } else {
            _incorrectAnswerSubmitted.value = true
        }
    }

    private fun selectLetter() {
        var pos = 0
        do {
            pos = (random() * 10).toInt()
        } while (pos !in (0..basicWord.value!!.length))
        correctPosition = pos
    }

    private fun generateWord() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWordToLetterGame()
                .collect {
                    _basicWord.value = it
                }
            selectLetter()
        }
    }

    class Factory(val repository: Repository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LetterGameViewModel(repository = repository) as T
        }
    }
}