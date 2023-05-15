package com.example.lexiapp.domain.repositories.objectives

import com.example.lexiapp.domain.model.Objective

object ObjectiveMocks {

    fun getObjectiveMocks(): List<Objective>{

        val objective1 = Objective.Builder()
            .id(111111L)
            .title("Jugar Palabra Correcta")
            .description("Para conseguir este objetivo debes jugar 3 veces el juego palabra correcta")
            .goal(3)
            .build()

        val objective2 = Objective.Builder()
            .id(222222L)
            .title("Jugar ¿Dónde está la letra?")
            .description("Para conseguir este objetivo debes jugar 2 veces el juego")
            .goal(2)
            .build()

        val objective3 = Objective.Builder()
            .id(333333L)
            .title("Jugar ¡Vamos a leer!")
            .description("Para conseguir este objetivo debes leer 1 texto")
            .goal(1)
            .build()

        val objective4 = Objective.Builder()
            .id(444444L)
            .title("Jugar ¿Así se dice?")
            .description("Para conseguir este objectivo debes leer 3 palabras")
            .goal(3)
            .build()

        return listOf(objective1, objective2, objective3, objective4)
    }

}