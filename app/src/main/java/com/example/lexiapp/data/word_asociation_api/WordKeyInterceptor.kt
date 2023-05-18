package com.example.lexiapp.data.word_asociation_api

import API_KEY_WORD_ASSOCIATION
import okhttp3.Interceptor
import okhttp3.Response

class WordKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest=chain.request()
        val modifiedUrl =originalRequest
            .url.newBuilder()
            /*.addQueryParameter("apikey", API_KEY_WORD_ASSOCIATION)*/
            .build()
        val modifiedRequest = originalRequest.newBuilder()
            .url(modifiedUrl)
            .build()
        return chain.proceed(modifiedRequest)
    }
}


