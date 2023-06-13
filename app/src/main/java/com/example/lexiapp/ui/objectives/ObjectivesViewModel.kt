package com.example.lexiapp.ui.objectives

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lexiapp.domain.model.Objective
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class ObjectivesViewModel : ViewModel() {
    private val _objectives = MutableLiveData<List<Objective>>()
    val objectives: LiveData<List<Objective>> = _objectives

    private val _daysLeft = MutableLiveData<Int>()
    val daysLeft: LiveData<Int> = _daysLeft

    fun loadObjectives() {
        val today = LocalDate.now()
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val objectives = getObjectives(monday)
        _objectives.value = objectives

        val daysLeft = calculateDaysLeft(monday)
        _daysLeft.value = daysLeft
    }

    private fun getObjectives(startDate: LocalDate): List<Objective> {
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

    private fun calculateDaysLeft(startDate: LocalDate): Int {
        val today = LocalDate.now()
        val nextMonday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.MONDAY))

        val daysLeft = nextMonday.dayOfWeek.value - today.dayOfWeek.value

        return if (daysLeft < 0) {
            7 - (today.dayOfWeek.value - nextMonday.dayOfWeek.value)
        } else {
            daysLeft
        }
    }
}
