package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

object TextPlaces: TextToReadInterface {
    override fun getTexts(): List<TextToRead> {
        val text1 = TextToRead.Builder()
            .id(1)
            .title("Isla del tesoro")
            .text("""
                Un grupo de amigos encuentra un mapa del tesoro que los lleva a una emocionante isla. ¿Podrán resolver los acertijos y encontrar el tesoro escondido?
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("Aventuras del bosque")
            .text("""
                Dos hermanos deciden explorar un misterioso bosque lleno de criaturas mágicas. Juntos, vivirán una increíble aventura llena de sorpresas
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("Monte de secretos")
            .text("""
                Un niño curioso descubre un antiguo secreto guardado en una montaña. Con la ayuda de nuevos amigos, desvelará el misterio y salvará el lugar
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3)
    }
}