package com.example.lexiapp.ui.games.whereistheletter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.domain.Repository
import com.example.lexiapp.domain.RepositoryImpl
import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.ui.games.letsread.TextViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WhereIsTheLetterViewModel: ViewModel() {
    lateinit var repository: Repository

    private var _selectedPosition = MutableStateFlow<Int?>(null)
    val selectedPosition = _selectedPosition.asStateFlow()

    private var _basicWord = MutableStateFlow("TESTEANDO")
    val basicWord = _basicWord.asStateFlow()

    private var _correctPosition = MutableStateFlow(2)
    val correctPosition = _correctPosition.asStateFlow()

    private var _correctAnswerSubmitted = MutableStateFlow(false)
    val correctAnswerSubmitted = _correctAnswerSubmitted.asStateFlow()

    private var _incorrectAnswerSubmitted = MutableStateFlow(false)
    val incorrectAnswerSubmitted = _incorrectAnswerSubmitted.asStateFlow()

    init {
        initRepository()
        generateWord()
    }

    private fun initRepository() {
        repository = RepositoryImpl()
    }

    fun onPositionSelected(position: Int) {
        _selectedPosition.value = position
    }

    fun onOmitWord() {
        generateWord()
    }

    fun onSubmitAnswer() {
        if(selectedPosition.value == correctPosition.value) {
            _correctAnswerSubmitted.value = true
        } else {
            _incorrectAnswerSubmitted.value = true
        }
    }

    private fun selectLetter() {
        var pos = 0
        do {
            pos = (Math.random() * 10).toInt()
        } while (pos !in (0..basicWord.value!!.length))
        _correctPosition.value = pos
    }

    private fun generateWord() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getWordToLetterGame()
                .collect {
                    _basicWord.value = it ?: "Testeando"
                }
            selectLetter()
        }
    }

    fun resetSubmit() {
        _correctAnswerSubmitted.value = false
        _incorrectAnswerSubmitted.value = false
    }

    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WhereIsTheLetterViewModel() as T
        }
    }
}