package com.example.lexiapp.ui.games.letsread

import androidx.lifecycle.*
import com.example.lexiapp.domain.model.TextToRead
import kotlinx.coroutines.launch

class TextViewModel: ViewModel() {
    private val _listText = MutableLiveData<List<TextToRead>>()
    val listText: LiveData<List<TextToRead>> = _listText
    private val _textSelected = MutableLiveData<TextToRead>()
    val textSelected: LiveData<TextToRead> = _textSelected

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


    private fun getText()=
        listOf(
            TextToRead(1,"jfidsjkjs","TXT 1"),
            TextToRead(2,"jfidsjkmvnlfkmvlkx00js","TXT 2"),
            TextToRead(3,"jsfdscsdcfidsjkjs","TXT 3"),
            TextToRead(4,"jvdfvdfvfidsjkjs","TXT 4"),
            TextToRead(5,"jfidsjvdsvdsvfdkjs","TXT 5"),
            TextToRead(6,"jffdsfdsvdfidsjkjs","TXT 6"),
            TextToRead(7,"jfidsjkjsgfdfvfgnhg","TXT 7"),
        )

}