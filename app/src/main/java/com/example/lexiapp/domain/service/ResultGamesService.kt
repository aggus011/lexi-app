package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.WhereIsTheLetterResult

interface ResultGamesService {

    fun getWhereIsTheLetterResults(emails: List<String>): List<WhereIsTheLetterResult>

}
