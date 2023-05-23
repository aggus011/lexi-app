package com.example.lexiapp.data.repository.texttoread

import com.example.lexiapp.domain.model.TextToRead

object TextToReadMocks {

    fun getAllTextToReadMocks(): List<TextToRead>{

        val text1 = TextToRead.Builder()
            .id(1)
            .title("Trabalenguas Fabula")
            .text("""
                Fabulas fabulosas hay
                en fabulosos fabularios,
                fabuladores y fabulistas
                hacen fábulas fabulosas
                pero la fabulosidad
                de las fábulas
                del fabulista
                no son fabulosas
                si no hace un fabulario
                de fábulas.
            """.trimIndent())
            .build()

        val text2 = TextToRead.Builder()
            .id(2)
            .title("La K de Ana Maria")
            .text("""
                Ana María Matute es reconocida como la escritora de mayor prestigio de las letras españolas. Nació (1925) en el seno de una familia acomodada, en Barcelona (España). Falleció el 25 de junio de 2014.

                Su padre, Facundo, fue propietario de una fábrica de paraguas y un viajero incansable. De uno de estos viajes le trajo un muñeco gordo que apodó como “Gorogó”; el cual conservaba con gran cariño.

                Su infancia (complicada por la Guerra Civil y las enfermedades); así como la gente que conoció en los diferentes lugares donde vivió, han sido siempre fuentes de inspiración, para escribir sus novelas

                Con 17 años escribió su primera novela “Pequeño Teatro”, por la que le ofrecieron su primer contrató (3000 pesetas) en la editorial Destino. Desde entonces su amor por la literatura le llevó a escribir más novelas, relatos cortos y cuentos para niños

                En 1996 fue elegida para ser miembro de la RAE (Real Academia Española) y ocupar el asiento “k”, un honor reservado para los mejores escritores

                Como buena escritora, Ana María, reconocía que le apasiona leer: “El primer ‘érase’ que oí conmovió mi vida” y mientras pueda seguirá “inventando, invenciones”
            """.trimIndent())
            .build()

        val text3 = TextToRead.Builder()
            .id(3)
            .title("Los reyes de la baraja")
            .text("""
                Si tu madre quiere un rey,
                la baraja tiene cuatro:
                rey de oros, rey de copas,
                rey de espadas, rey de basto
                Corre que te pillo,
                corre que te agarro,
                mira que te lleno
                la cara de barro.
                Del olivo
                me retiro,
                del esparto
                yo me aparto,
                del sarmiento
                me arrepiento
                de haberte querido tanto.
            """.trimIndent())
            .build()

        val text4 = TextToRead.Builder()
            .id(4)
            .title("El papel y la tinta")
            .text("""
                Había una hoja de papel sobre una mesa, junto a otras hojas iguales a ella, cuando una pluma, bañada en negrísima tinta, la manchó completa y la llenó de palabras.

                – “¿No podrías haberme ahorrado esta humillación?”, dijo enojada la hoja de papel a la tinta. “Tu negro infernal me ha arruinado para siempre”.

                – “No te he ensuciado”, repuso la tinta. “Te he vestido de palabras. Desde ahora ya no eres una hoja de papel sino un mensaje. Custodias el pensamiento del hombre. Te has convertido en algo precioso”.

                En ese momento, alguien que estaba ordenando el despacho, vio aquellas hojas esparcidas y las juntó para arrojarlas al fuego. Sin embargo, reparó en la hoja “sucia” de tinta y la devolvió a su lugar porque llevaba, bien visible, el mensaje de la palabra. Luego, arrojó el resto al fuego.
            """.trimIndent())
            .build()

        return listOf(text1, text2, text3, text4)
    }

}