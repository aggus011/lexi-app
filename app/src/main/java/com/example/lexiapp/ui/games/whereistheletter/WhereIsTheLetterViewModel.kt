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
    /*
    private val _wordToPlay = MutableLiveData<String>()
    val wordToPlay: LiveData<String> = _wordToPlay
    private val _letter = MutableLiveData<String>()
    val letter: LiveData<String> = _letter
    private val _letterSelected = MutableLiveData<String>()
    val letterSelected: LiveData<String> = _letterSelected

    init {
        setValues()
    }

    private fun setValues() {
        _wordToPlay.value=""
        _letter.value=""
        _wordToPlay.value="Comodín"
        _letter.value="d"
    }
     */

    private var _selectedPosition = MutableStateFlow<Int?>(null)
    val selectedPosition = _selectedPosition.asStateFlow()

    private var _basicWord = MutableStateFlow<String?>(null)
    val basicWord = _basicWord.asStateFlow()

    private var _correctPosition = MutableStateFlow(-1)
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
                    _basicWord.value = it
                }
            selectLetter()
        }
    }

    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WhereIsTheLetterViewModel() as T
        }
    }
}