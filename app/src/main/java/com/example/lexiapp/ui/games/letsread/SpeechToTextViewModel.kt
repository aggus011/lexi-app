package com.example.lexiapp.ui.games.letsread

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lexiapp.data.api.SpeechToTextRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Error
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class SpeechToTextViewModel @Inject constructor(private val speechRepository: SpeechToTextRepository) :
    ViewModel() {

    private val transcription: MutableLiveData<String> = MutableLiveData()


    fun transcription(path: File) = CoroutineScope(Dispatchers.IO).launch {
        try {
            Log.d(TAG, "que envio con path $path")
            val result = speechRepository.transcription(path)
            Log.d(TAG, "result $result")
            if (result.body() != null)
                transcription.postValue(result.body()!!.text)
        } catch (e: NullPointerException) {
            Log.e(TAG, "Null error! $e")
        } catch (e: Error) {
            Log.e(TAG, "Error! $e")
        }
    }

    companion object{
        const val TAG = "SpeechToTextViewModel"
    }
}