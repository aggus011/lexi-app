package com.example.lexiapp.data.model

import com.example.lexiapp.domain.model.User

data class GameResult(
    val game: Game,
    val user_mail: String,
    val result: Any
)
