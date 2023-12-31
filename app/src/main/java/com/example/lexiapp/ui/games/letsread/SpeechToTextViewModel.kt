package com.example.lexiapp.ui.games.letsread

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lexiapp.domain.useCases.SpeechToTextUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.lang.Error
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class SpeechToTextViewModel @Inject constructor(
    private val speechToTextUseCases: SpeechToTextUseCases
    ) : ViewModel() {

    val transcription: MutableLiveData<String> = MutableLiveData()

    fun transcription(path: MultipartBody.Part) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val result = speechToTextUseCases.transcription(path)
            if (result.body() != null)
                transcription.postValue(result.body()!!.text)
        } catch (e: NullPointerException) {
            Log.e(TAG, "Null error! $e")
        } catch (e: Error) {
            Log.e(TAG, "Error! $e")
        }
    }

    companion object {
        const val TAG = "SpeechToTextViewModel"
    }
}