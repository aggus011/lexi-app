package com.example.lexiapp.ui.games.whereistheletter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lexiapp.domain.model.TextToRead
import com.example.lexiapp.ui.games.letsread.TextViewModel

class WhereIsTheLetterViewModel: ViewModel() {
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

    class Factory: ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return WhereIsTheLetterViewModel() as T
        }
    }
}