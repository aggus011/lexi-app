package com.example.lexiapp.data.model

import com.example.lexiapp.domain.model.User

abstract class GameResult(
    open val game: Game,
    open val user_mail: String,
    open val result: Any
)
