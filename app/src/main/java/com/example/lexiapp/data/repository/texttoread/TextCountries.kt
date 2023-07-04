package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

object TextCountries: TextToReadInterface {
    override fun getTexts(): List<TextToRead> {
        val text1 = TextToRead.Builder()
            .id(1)
            .title("Viaje a Kenya")
            .text("""
               Un grupo de amigos viaja a Kenya y se maravilla con la belleza de la naturaleza y los animales. Aprenderán sobre la cultura y costumbres del país africano 
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("Aventura en Italia")
            .text("""
               Dos hermanos visitan Italia y exploran ciudades históricas como Roma y Venecia. Descubrirán monumentos famosos y probarán deliciosas comidas italianas 
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("Fiesta en Brasil")
            .text("""
                Un niño y su familia van a Brasil y participan en el carnaval más grande del mundo. Bailarán samba, verán desfiles coloridos y disfrutarán de la música y la alegría brasileña
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3)
    }
}