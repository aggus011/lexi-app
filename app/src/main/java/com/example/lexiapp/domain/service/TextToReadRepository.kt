package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.TextToRead

interface TextToReadRepository {

    fun getTextToRead(categories: List<String>): List<TextToRead>

}
