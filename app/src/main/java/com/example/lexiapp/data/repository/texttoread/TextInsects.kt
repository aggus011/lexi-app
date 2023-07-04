package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

object TextInsects: TextToReadInterface {
    override fun getTexts(): List<TextToRead> {
        val text1 = TextToRead.Builder()
            .id(1)
            .title("El Abejorro Valiente")
            .text("""
              El abejorro es un insecto peludo y simpático. Vuela de flor en flor buscando néctar y polen. Aunque es grande y ruidoso, es inofensivo y no pica. Acompaña al abejorro en su aventura por el jardín y descubre por qué es tan importante para las plantas  
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("La Mariposa Colorida")
            .text("""
                La mariposa es un insecto hermoso y delicado. Con sus alas llenas de colores, revolotea de flor en flor. Algunas mariposas migran miles de kilómetros en busca de buen clima. Aprende sobre la metamorfosis y admira la belleza de las mariposas
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("El Saltamontes Veloz")
            .text("""
                El saltamontes es un insecto ágil y saltarín. Con sus patas largas, salta de hoja en hoja en el campo. Produce un sonido peculiar al frotar sus alas. Descubre cómo se camufla y diviértete con las acrobacias del saltamontes
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3)
    }
}