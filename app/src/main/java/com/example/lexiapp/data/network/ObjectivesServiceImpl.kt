package com.example.lexiapp.data.network

import ObjectiveMocks
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.service.ObjectivesService
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import javax.inject.Inject


class ObjectivesServiceImpl @Inject constructor(
    private val db: FireStoreServiceImpl
    ) : ObjectivesService {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private var uid = currentUser?.uid.toString()

    private val objectiveMocks = ObjectiveMocks

    override fun getObjectives(startDate: LocalDate): List<Objective> {
        return objectiveMocks.getObjectiveMocks()
    }

    override suspend fun getCompleteObjectives() = db.getObjectivesHistory(uid)

    override suspend fun checkObjectivesExist(lastMondayDate: String) = db.checkObjectivesExist(uid, lastMondayDate)

    override suspend fun saveObjectives(objectives: List<Objective>) = db.saveObjectives(uid, objectives)

    override suspend fun getIncompleteGameNames(lastMondayDate: String): List<String> = db.getIncompleteGameNames(uid, lastMondayDate)

    override suspend fun increaseGoalForGames(games: List<String>) = db.increaseGoalForGames(uid, games)

    override suspend fun getObjectivesActual(lastMondayDate: String, listener: (List<Objective>) -> Unit) = db.getObjectives(uid, lastMondayDate, listener)


    }
