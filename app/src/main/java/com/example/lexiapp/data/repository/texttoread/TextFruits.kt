package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

object TextFruits: TextToReadInterface {
    override fun getTexts(): List<TextToRead> {
        val text1 = TextToRead.Builder()
            .id(1)
            .title("Aventuras de manzana")
            .text("""
                Luz, una niña curiosa, descubre un árbol mágico de manzanas. Junto a su amigo Carlos, exploran el poder de esta fruta y aprenden sobre una alimentación saludable
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("Misteriosa fresa")
            .text("""
                Sofía y Alejandro encuentran una fresa parlante en el jardín. Juntos, siguen las pistas para resolver un enigma y descubrirán los beneficios nutritivos de esta deliciosa fruta
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("Naranja viajera")
            .text("""
                Pedro se embarca en un viaje lleno de aventuras con una naranja mágica. A medida que explora diferentes lugares, aprenderá sobre los beneficios de esta fruta y cómo mantenerse saludable
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3)
    }
}