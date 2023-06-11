package com.example.lexiapp.ui.objectives

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.R
import com.example.lexiapp.databinding.FragmentObjectivesBinding
import com.example.lexiapp.ui.adapter.ObjectivesAdapter
import com.example.lexiapp.domain.model.Objective
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class ObjectivesFragment : Fragment() {
    private var _binding: FragmentObjectivesBinding? = null
    private val binding get() = _binding!!

    private lateinit var objectiveList: List<Objective>
    private lateinit var rvObjectives: RecyclerView
    private lateinit var objectivesAdapter: ObjectivesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObjectivesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        loadObjectives()
    }

    private fun setUpRecyclerView() {
        rvObjectives = binding.rvObjectives
        objectivesAdapter = ObjectivesAdapter()
        rvObjectives.layoutManager = LinearLayoutManager(requireContext())
        rvObjectives.adapter = objectivesAdapter
    }

    private fun loadObjectives() {
        val today = LocalDate.now()
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val objectives = getObjectives(monday)
        objectivesAdapter.updateObjectiveList(objectives)

        val daysLeft = calculateDaysLeft(monday)
        binding.txtDaysLeft.text = resources.getQuantityString(R.plurals.days_left, daysLeft, daysLeft)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
