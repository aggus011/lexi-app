package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

object TextVegetables: TextToReadInterface {
    override fun getTexts(): List<TextToRead> {
        val text1 = TextToRead.Builder()
            .id(1)
            .title("El Gran Cultivo")
            .text("""
                Jazmín, una niña ingeniosa, convierte su jardín en un huerto mágico. Cuidando de sus verduras, aprenderá sobre la importancia de una alimentación saludable
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("La Aventura Verde")
            .text("""
                Martín, un niño aventurero, se encoge y explora el mundo de las verduras. Juntos, enfrentarán desafíos y descubrirán el poder nutritivo de estos vegetales
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("Secretos de zanahoria")
            .text("""
                Luciano encuentra una zanahoria mágica en el supermercado. Al morderla, se convierte en un superhéroe con súper fuerza. Aprenderá que las verduras son la clave para tener poderes increíbles
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3)
    }
}