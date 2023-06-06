package com.example.lexiapp.ui.games.letsread

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lexiapp.data.api.SpeechToTextRepository
import com.example.lexiapp.data.api.word_asociation_api.model.Rows
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Error
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class SpeechToTextViewModel @Inject constructor(private val speechRepository: SpeechToTextRepository) :
    ViewModel() {

    val transcription: MutableLiveData<String> = MutableLiveData()
    val difference: MutableLiveData<Rows> = MutableLiveData()
    private lateinit var original: String
    private lateinit var revised: String


    fun transcription(path: RequestBody) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val result = speechRepository.transcription(path)
            Log.d(TAG, "result $result code ${result.code()}")
            if (result.body() != null) {
                transcription.postValue(result.body()!!.text)
                Log.d(TAG, "result body ${result.body()}")
            }
        } catch (e: NullPointerException) {
            Log.e(TAG, "Null error! $e")
        } catch (e: Error) {
            Log.e(TAG, "Error! $e")
        }
    }

    fun getDifference(originalText: String, revisedText: String) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                original = originalText
                revised = revisedText
                val result = speechRepository.getDifference(originalText, revisedText)
                if (result.body() != null)
                    difference.postValue(result.body()!!)
            } catch (e: NullPointerException) {
                Log.e(TAG, "Call is null $e")
            } catch (e: Error) {
                Log.e(TAG, "Error! $e")
            }
        }

    fun convertToText(): String {
        return getStringBuilder(original)
    }

    private fun getStringBuilder(originalText: String): String {
        val diffBuilder = StringBuilder()
        var errors = 0
        for (f in difference.value!!.rows[0].right.chunks) {
            when (f.type) {
                "equal" -> diffBuilder.append(f.value)
                "insert" -> {
                    diffBuilder.append("<font color='#FF0000'>${f.value}</font> ")
                    errors++
                }

                "removed" -> diffBuilder.append("<font color='#FF0000'>---</font>")
                else -> break
            }
        }
        val total = wordCounter(originalText)
        val average = errors * 100 / total
        return if (average >= 10)
            diffBuilder.toString()
        else "Correct"
    }

    private fun wordCounter(texto: String): Int {
        val words = texto.trim().split("\\s+".toRegex())
        return words.size
    }

    companion object{
        const val TAG = "SpeechToTextViewModel"
    }
}