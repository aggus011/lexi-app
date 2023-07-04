package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

object TextVehicles: TextToReadInterface {
    override fun getTexts(): List<TextToRead> {
        val text1 = TextToRead.Builder()
            .id(1)
            .title("El Auto Veloz")
            .text("""
               Un auto llamado Max descubre que tiene un motor mágico y puede correr muy rápido. Junto a sus amigos, vivirá emocionantes aventuras en la pista de carreras 
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("Aventura en bicicleta")
            .text("""
                Luis encuentra una bicicleta abandonada y decide arreglarla. Con su nueva bicicleta, explorará su vecindario, descubriendo nuevos lugares y haciendo amigos
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("Viaje en barco")
            .text("""
                Sofía y su familia deciden tomar unas vacaciones en un barco. Navegarán por ríos y océanos, viendo delfines y ballenas. ¡Será una aventura acuática inolvidable!
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3)
    }
}