package com.example.lexiapp.ui.games.isitsocalled

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lexiapp.domain.useCases.CorrectWordUseCases
import com.example.lexiapp.domain.useCases.IsItSoCalledUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IsItSoCalledViewModel@Inject constructor(
    private val isIsItSoCalledUseCases: IsItSoCalledUseCases
) : ViewModel() {

    private val _phrase = MutableLiveData<String?>()
    val phrase: LiveData<String?> = _phrase
    val visibilityBtnsResult = MutableLiveData<Boolean>(true)

    init {
        visibilityBtnsResult.value=true
    }

    fun setVisibility(){
        val i = visibilityBtnsResult.value!!
        visibilityBtnsResult.value=!i
    }

}