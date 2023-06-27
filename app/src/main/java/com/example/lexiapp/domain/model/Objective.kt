package com.example.lexiapp.domain.model

class Objective(
    val id: Long?,
    val title: String?,
    val description: String?,
    val progress: Int,
    val goal: Int?,
    val game: String?,
    val type: String?,
    val completed: Boolean?,
    val date: String?
) {

    data class Builder(
        var id: Long? = null,
        var title: String? = null,
        var description: String? = null,
        var progress: Int = 0,
        var goal: Int? = null,
        var game: String? = null,
        var type: String? = null,
        var completed: Boolean? = null,
        var date: String? = null
    ) {
        fun id(id: Long?) = apply { this.id = id }
        fun title(title: String?) = apply { this.title = title }
        fun description(description: String?) = apply { this.description = description }
        fun progress(progress: Int = 0) = apply { this.progress = progress }
        fun goal(goal: Int?) = apply { this.goal = goal }
        fun game(game: String) = apply { this.game = game }
        fun type(type: String?) = apply { this.type = type }
        fun completed(completed: Boolean) = apply { this.completed = completed }
        fun date(date: String) = apply { this.date = date }

        fun build() = Objective(id, title, description, progress, goal, game, type, completed, date)
    }
}

data class MiniObjective(
    var date: String,
    val title: String,
    val count: Long
)
