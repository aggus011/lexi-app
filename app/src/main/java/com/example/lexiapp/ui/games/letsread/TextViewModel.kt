package com.example.lexiapp.ui.games.letsread

import androidx.lifecycle.*
import com.example.lexiapp.data.repository.texttoread.TextToReadMocks
import com.example.lexiapp.domain.model.TextToRead
import kotlinx.coroutines.launch

class TextViewModel: ViewModel() {
    private val _listText = MutableLiveData<List<TextToRead>>()
    val listText: LiveData<List<TextToRead>> = _listText

    init {
        _listText.value = emptyList()
        _listText.value= getText()
        /*viewModelScope.launch {
            _listText.value = getText()
        }*/
    }

    class Factory() : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TextViewModel() as T
        }
    }


    private fun getText() = TextToReadMocks.getAllTextToReadMocks()

}