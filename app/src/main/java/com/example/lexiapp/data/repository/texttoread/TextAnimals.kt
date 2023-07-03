package com.example.lexiapp.data.repository.texttoread
import com.example.lexiapp.domain.model.TextToRead

object TextAnimals: TextToReadInterface {
    override fun getTexts(): List<TextToRead> {

        val text1 = TextToRead.Builder()
            .id(1)
            .title("El Amigo Leal")
            .text("""
                Lola, una niña aventurera, encuentra un cachorro abandonado en el parque. Juntos, vivirán emocionantes travesuras y aprenderán el valor de la amistad
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("El Salto Valiente")
            .text("""
                Pablo, un conejito tímido, sueña con superar su miedo a saltar. Con la ayuda de nuevos amigos, descubrirá su valentía y participará en una competencia de saltos
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("Aventura del Plumaje")
            .text("""
                Lucía, una niña curiosa, descubre un pavo real perdido en su jardín. Juntos, se embarcarán en una colorida aventura para ayudarlo a encontrar su hogar
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3)
    }
}