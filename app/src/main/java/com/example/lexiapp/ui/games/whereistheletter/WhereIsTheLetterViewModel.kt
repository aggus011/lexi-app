package com.example.lexiapp.ui.games.whereistheletter

import android.util.Log
import androidx.lifecycle.*
import com.example.lexiapp.domain.model.gameResult.WhereIsTheLetterResult
import com.example.lexiapp.domain.useCases.LetterGameUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WhereIsTheLetterViewModel @Inject constructor(
    private val letterGameUseCases: LetterGameUseCases
) : ViewModel() {

    private var _selectedPosition = MutableStateFlow<Int?>(null)
    var selectedPosition: LiveData<Int?> = _selectedPosition.asLiveData()

    private var _basicWord = MutableStateFlow<String?>(null)
    var basicWord: LiveData<String?> = _basicWord.asLiveData()

    private var _correctPosition = MutableStateFlow(2)

    private var _correctAnswerSubmitted = MutableStateFlow(false)
    val correctAnswerSubmitted = _correctAnswerSubmitted.asStateFlow()

    private var _incorrectAnswerSubmitted = MutableStateFlow(false)

    private var _letter = MutableStateFlow('*')
    var letter: LiveData<Char> = _letter.asLiveData()

    fun onPositionSelected(position: Int) {
        _selectedPosition.value = position
    }

    fun onPositionDeselected() {
        _selectedPosition.value = null
    }

    fun isItSelected(position: Int) = _selectedPosition.value == position

    fun isAnyLetterSelected() = _selectedPosition.value != null

    fun onOmitWord() {
        resetSubmit()
        generateWord()
        Log.v("HAVE_A_WORD", "WORD: ${basicWord.value}, LETTER: ${letter.value}")
    }

    fun onSubmitAnswer() {
        val success = validateAnswer()
        Log.v("SUCCES_ANSWER", "$success")
        viewModelScope.launch(Dispatchers.IO) {
            val result= WhereIsTheLetterResult(
                    email= "",
                    mainLetter = letter.value!!,
                    selectedLetter = getLetterWithPosition()!!,
                    word = basicWord.value!!,
                    success = success,
                    date = null
                )
            letterGameUseCases.saveWordInFirebase(result)
        }
    }

    private fun validateAnswer()=
        if (_selectedPosition.value == _correctPosition.value || checkChar()) {
            _correctAnswerSubmitted.value = true
            Log.v("SAVE_ANSWER", "${_selectedPosition.value}//${_correctPosition.value}//${checkChar()}")
            true
        } else {
            Log.v("SAVE_ANSWER", "${_selectedPosition.value}//${_correctPosition.value}//${checkChar()}//${getLetterWithPosition()}")
            _incorrectAnswerSubmitted.value = true
            false
        }

    private fun checkChar() = if (_selectedPosition.value != null)
        _basicWord.value!![_selectedPosition.value!!] == _basicWord.value!![_correctPosition.value] else false


    private fun selectLetter() {
        var position: Int
        do {
            position = (Math.random() * 10).toInt()
        } while (position !in (0 until (_basicWord.value!!.length)))
        _correctPosition.value = position
        _letter.value = _basicWord.value!![position]
    }

    private fun generateWord() {
        viewModelScope.launch(Dispatchers.IO) {
            letterGameUseCases.getWord()
                .collect {
                    Log.v("data_in_view_model", "response word: $it")
                    _basicWord.value = it
                    Log.v(
                        "asignate_data_to_variable",
                        "response _basicWord: ${_basicWord.value} and basicWord: ${basicWord.value}"
                    )
                    _basicWord.value = it
                    Log.v(
                        "asignate_data_to_variable",
                        "response _basicWord: ${_basicWord.value} and basicWord: ${basicWord.value}"
                    )
                    selectLetter()
                }
        }
    }

    private fun resetSubmit() {
        _basicWord.value = null
        _selectedPosition.value = null
        _correctAnswerSubmitted.value = false
        _incorrectAnswerSubmitted.value = false
    }

    private fun getLetterWithPosition() = _basicWord.value?.get(_selectedPosition.value!!)

    fun getCorrectPosition() = _correctPosition.value

    fun getWord() = _basicWord.value

    fun getSelectedPosition() = _selectedPosition.value

}