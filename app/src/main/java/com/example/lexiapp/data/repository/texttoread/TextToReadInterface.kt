package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

interface TextToReadInterface {
    fun getTexts(): List<TextToRead>
}