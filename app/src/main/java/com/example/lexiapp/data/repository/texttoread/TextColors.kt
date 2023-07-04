package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

object TextColors: TextToReadInterface {
    override fun getTexts(): List<TextToRead> {
        val text1 = TextToRead.Builder()
            .id(1)
            .title("El fuego")
            .text("""
                El rojo es un color que nos hace pensar en el fuego. Es cálido y lleno de energía. ¡Descubre el poder del color rojo!
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("El cielo")
            .text("""
                El azul nos recuerda al cielo despejado. Es un color tranquilo y sereno. ¡Vuela por el mundo azul y sueña alto!
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("La naturaleza")
            .text("""
                El verde es el color de la naturaleza. Nos rodea de árboles, plantas y vida. ¡Adéntrate en el mundo verde y descubre su belleza!
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3)
    }
}