package com.example.lexiapp.domain.service

interface TextScannerService {

    suspend fun generateNotificationIfObjectiveHasBeenCompleted(game: String, typeGame: String, gameName: String)

}