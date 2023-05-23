package com.example.lexiapp.domain.model

class TextToRead(
    val id: Int?,
    val title: String?,
    val text: String?,
    val isRead: Int = 0){

    data class Builder(
        var id: Int? = null,
        var title: String? = null,
        var text: String? = null,
        var isRead: Int = 0){

        fun id(id: Int?) = apply { this.id = id }
        fun title(title: String?) = apply { this.title = title }
        fun text(text: String?) = apply { this.text = text }
        fun isRead(isRead: Int = 0) = apply { this.isRead = isRead}
        fun build() = TextToRead(id, title, text, isRead)
    }
}
