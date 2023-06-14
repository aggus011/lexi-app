package com.example.lexiapp.domain.model

class Objective(
    val id: Long?,
    val title: String?,
    val description: String?,
    val progress: Int = 0,
    val goal: Int?){

    data class Builder(
        var id: Long? = null,
        var title: String? = null,
        var description: String? = null,
        var progress: Int = 0,
        var goal: Int? = null){

        fun id(id: Long?) = apply { this.id = id }
        fun title(title: String?) = apply { this.title = title }
        fun description(description: String?) = apply { this.description = description }
        fun progress(progress: Int = 0) = apply { this.progress = progress }
        fun goal(goal: Int?) = apply { this.goal = goal }
        fun build() = Objective(id, title, description, progress, goal)
    }


}
