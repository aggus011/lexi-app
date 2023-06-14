package com.example.lexiapp.data.repository.objectives

import com.example.lexiapp.domain.model.Objective

object ObjectiveMocks {

    fun getObjectiveMocks(): List<Objective>{

        val allObjectives = listOf(
            Objective.Builder()
                .id(1L)
                .title("Jugar Palabra Correcta")
                .description("Descripción de Jugar Palabra Correcta")
                .progress(0)
                .goal(4)
                .build(),
            Objective.Builder()
                .id(2L)
                .title("Jugar ¡Vamos a Leer!")
                .description("Descripción de Jugar ¡Vamos a Leer!")
                .progress(0)
                .goal(3)
                .build(),
            Objective.Builder()
                .id(3L)
                .title("Jugar ¿Donde esta la Letra?")
                .description("Descripción de Jugar ¿Donde esta la Letra?")
                .progress(0)
                .goal(2)
                .build(),
            Objective.Builder()
                .id(4L)
                .title("Jugar ¿Así se Dice?")
                .description("Descripción de Jugar ¿Así se Dice?")
                .progress(0)
                .goal(2)
                .build(),
            Objective.Builder()
                .id(5L)
                .title("Escanear un Texto Propio")
                .description("Descripción de Escanear un Texto Propio")
                .progress(0)
                .goal(1)
                .build(),
            Objective.Builder()
                .id(6L)
                .title("Jugar Lectura Desafío")
                .description("Descripción de Jugar Lectura Desafío")
                .progress(0)
                .goal(2)
                .build(),
            Objective.Builder()
                .id(7L)
                .title("Acertar en Palabra Correcta")
                .description("Descripción de Acertar en Palabra Correcta")
                .progress(0)
                .goal(3)
                .build(),
            Objective.Builder()
                .id(8L)
                .title("Acertar en ¡Vamos a Leer!")
                .description("Descripción de Acertar en ¡Vamos a Leer!")
                .progress(0)
                .goal(2)
                .build(),
            Objective.Builder()
                .id(9L)
                .title("Acertar en ¿Donde esta la Letra?")
                .description("Descripción de Acertar en ¿Donde esta la Letra?")
                .progress(0)
                .goal(3)
                .build(),
            Objective.Builder()
                .id(10L)
                .title("Acertar en ¿Así se Dice?")
                .description("Descripción de Acertar en ¿Así se Dice?")
                .progress(0)
                .goal(2)
                .build()
        )

        return allObjectives.shuffled().take(4)
    }

}