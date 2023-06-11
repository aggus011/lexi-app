package com.example.lexiapp.data.api.openaicompletions

import android.util.Log
import com.example.lexiapp.data.api.openaicompletions.model.OpenAICompletionsRequest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OpenAICompletionsGateway @Inject constructor(
    private val client: OpenAICompletionsClient
) {
    suspend fun getChallengeReading(prompt: String, challengeWords: List<String>) = flow {
        try {
            val response = client.createChallengeReading(
                OpenAICompletionsRequest(prompt = prompt.plus(challengeWords))
            )
            val challengeReading: String =
                if(response.isSuccessful && response.body() != null) {
                    response.body()!!.choiceList[0].text
                }else{
                    ""
                }
            emit(challengeReading)
        }catch (e: Exception){
            Log.v(TAG, e.message.toString())
        }
    }

    companion object{
        const val TAG = "OpenAICompletionsService Exception"
    }
}