package com.example.lexiapp.ui.objectives

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lexiapp.data.api.openaicompletions.OpenAICompletionsGateway.Companion.TAG
import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.useCases.ObjectivesUseCases
import com.example.lexiapp.domain.service.FireStoreService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class ObjectivesViewModel @Inject constructor(
    private val objectivesUseCases: ObjectivesUseCases,
    private val fireStoreService: FireStoreService
) : ViewModel() {

    private val _objectives = MutableLiveData<List<Objective>>()
    val objectives: LiveData<List<Objective>> = _objectives

    private val _daysLeft = MutableLiveData<Int>()
    val daysLeft: LiveData<Int> = _daysLeft

    init {
        loadObjectives()
    }

    fun loadObjectives() {
        val today = LocalDate.now()
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        viewModelScope.launch {
            val objectives = fireStoreService.getObjectives("asd10@asd.com")
            _objectives.value = objectives

            val daysLeft = objectivesUseCases.calculateDaysLeft(monday)
            _daysLeft.value = daysLeft
        }
    }


    fun saveObjectivesToFirestore(email: String) {
        viewModelScope.launch {
            val objectivesExist = fireStoreService.checkObjectivesExist(email)
            if (!objectivesExist) {
                val today = LocalDate.now()
                val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val objectives = objectivesUseCases.getObjectives(monday)

                fireStoreService.saveObjectives(email, objectives ?: emptyList())
            }
        }
    }
    }



