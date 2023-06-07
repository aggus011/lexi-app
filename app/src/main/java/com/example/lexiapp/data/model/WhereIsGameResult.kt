package com.example.lexiapp.data.model

data class WhereIsGameResult(
    override val game: Game,
    override val user_mail: String,
    override val result: Pair<String, String>
) : GameResult(
    game, user_mail,
    result
)
