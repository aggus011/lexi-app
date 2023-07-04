package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

object TextNames: TextToReadInterface {
    override fun getTexts(): List<TextToRead> {
        val text1 = TextToRead.Builder()
            .id(1)
            .title("Secreto de Ana")
            .text("""
                Ana descubre un libro mágico que la transporta a un mundo lleno de aventuras y personajes fantásticos. Con valentía y creatividad, vivirá increíbles historias
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("El Misterio")
            .text("""
                Max es un niño curioso que siempre está en busca de nuevos enigmas para resolver. Con la ayuda de sus amigos, desentrañará un misterio emocionante en su vecindario
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("Aventura de Leo")
            .text("""
                Leo es un explorador intrépido que viaja por el mundo en busca de tesoros ocultos. En cada destino, vivirá emocionantes descubrimientos y conocerá culturas fascinantes
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3)
    }
}