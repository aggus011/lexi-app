package com.example.lexiapp.data.network

import com.example.lexiapp.data.api.difference_text.DifferenceGateway
import com.example.lexiapp.data.model.toLetsReadGameDataResult
import com.example.lexiapp.domain.model.gameResult.LetsReadGameResult
import com.example.lexiapp.domain.service.FireStoreService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DifferenceServiceImplTest {

    @RelaxedMockK
    private lateinit var apiDifferenceGateway: DifferenceGateway
    @RelaxedMockK
    private lateinit var db: FireStoreService
    @RelaxedMockK
    private lateinit var notificationServiceImpl: FirebaseNotificationServiceImpl

    lateinit var differenceService: DifferenceServiceImpl

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        coEvery { db.getDeviceToken() } returns diviceToken
        differenceService = DifferenceServiceImpl(apiDifferenceGateway, db, notificationServiceImpl)
    }

    @Test
    fun `when try to save a positive result, save the result and update the play and hit progress`() = runBlocking {
        //Given
        val result = LetsReadGameResult(
            email = emailTest,
            success = positiveSuccess,
            wrongWords = wrongWords,
            totalWords = totalWords,
            date = date
        )
        //When
        differenceService.saveLetsReadResult(result, isNotChallenge)
        //Then
        coVerify (exactly = 1) { db.saveLetsReadResult(result.toLetsReadGameDataResult()) }
        coVerify (exactly = 1) { db.updateObjectiveProgress("LR", "play") }
        coVerify (exactly = 1) { db.updateObjectiveProgress("LR", "hit") }
    }

    @Test
    fun `when try to save a negative result, save the result and update the play progress`() = runBlocking {
        //Given
        val result = LetsReadGameResult(
            email = emailTest,
            success = negativeSuccess,
            wrongWords = wrongWords,
            totalWords = totalWords,
            date = date
        )
        //When
        differenceService.saveLetsReadResult(result, isNotChallenge)
        //Then
        coVerify (exactly = 1) { db.saveLetsReadResult(result.toLetsReadGameDataResult()) }
        coVerify (exactly = 1) { db.updateObjectiveProgress("LR", "play") }
        coVerify (exactly = 0) { db.updateObjectiveProgress("LR", "hit") }

    }

    @Test
    fun `when try to save a positive challengeResult, save the result and update the play and hit progress`() = runBlocking {
        //Given
        val result = LetsReadGameResult(
            email = emailTest,
            success = positiveSuccess,
            wrongWords = wrongWords,
            totalWords = totalWords,
            date = date
        )
        //When
        differenceService.saveLetsReadResult(result, isChallenge)
        //Then
        coVerify (exactly = 1) { db.saveLetsReadResult(result.toLetsReadGameDataResult()) }
        coVerify (exactly = 1) { db.updateObjectiveProgress("RC", "play") }
        coVerify (exactly = 1) { db.updateObjectiveProgress("RC", "hit") }
    }

    @Test
    fun `when try to save a negative challengeResult, save the result and update the play and hit progress`() = runBlocking {
        //Given
        val result = LetsReadGameResult(
            email = emailTest,
            success = negativeSuccess,
            wrongWords = wrongWords,
            totalWords = totalWords,
            date = date
        )
        //When
        differenceService.saveLetsReadResult(result, isChallenge)
        //Then
        coVerify (exactly = 1) { db.saveLetsReadResult(result.toLetsReadGameDataResult()) }
        coVerify (exactly = 1) { db.updateObjectiveProgress("RC", "play") }
        coVerify (exactly = 0) { db.updateObjectiveProgress("RC", "hit") }
    }

    @Test
    fun `when an objectivePlay is complete, send the notification of that objective`() = runBlocking {
        //Given
        coEvery { db.checkIfObjectiveAndWeeklyObjectivesWereCompleted(typeGameLR, typePlay) } returns objectiveComplete
        //When
        differenceService.generateNotificationIfObjectiveHasBeenCompleted(typeGameLR, typePlay, nameGame)
        //Then
        coVerify (exactly = 1) { notificationServiceImpl.sendNotificationObjectiveCompleted(
            diviceToken, "Jugar $nameGame")
        }
        coVerify (exactly = 0) { notificationServiceImpl.sendNotificationWeeklyObjectivesCompleted(diviceToken) }
    }

    @Test
    fun `when an objectiveHit is complete, send the notification of that objective`() = runBlocking {
        //Given
        coEvery { db.checkIfObjectiveAndWeeklyObjectivesWereCompleted(typeGameRC, typeHit) } returns objectiveComplete
        //When
        differenceService.generateNotificationIfObjectiveHasBeenCompleted(typeGameRC, typeHit, nameGame)
        //Then
        coVerify (exactly = 1) { notificationServiceImpl.sendNotificationObjectiveCompleted(
            diviceToken, "Acertar en $nameGame")
        }
        coVerify (exactly = 0) { notificationServiceImpl.sendNotificationWeeklyObjectivesCompleted(diviceToken) }
    }

    @Test
    fun `when all objectives of week are complete, send the weekly notification`() = runBlocking {
        //Given
        coEvery { db.checkIfObjectiveAndWeeklyObjectivesWereCompleted(typeGameRC, typeHit) } returns allObjectivesComplete
        coEvery { db.getDeviceToken() } returns diviceToken
        //When
        differenceService.generateNotificationIfObjectiveHasBeenCompleted(typeGameRC, typeHit, nameGame)
        //Then
        coVerify (exactly = 0) { notificationServiceImpl.sendNotificationObjectiveCompleted(
            diviceToken, "Acertar $nameGame")
        }
        coVerify (exactly = 1) { notificationServiceImpl.sendNotificationWeeklyObjectivesCompleted(diviceToken) }
    }
}

const val emailTest = "test@gmail.com"
const val positiveSuccess = true
const val negativeSuccess = false
const val isChallenge = true
const val isNotChallenge = false
val wrongWords = listOf("CASA", "PATO", "BOTA")
const val totalWords = 20
var date = System.currentTimeMillis().toString()
const val diviceToken = "e3eHzqEbRo2JsonyA5dapb:A"
val objectiveComplete = Pair(true, false)
val allObjectivesComplete = Pair(true, true)
const val nameGame = "Vamos a Leer"
const val typeGameLR = "LR"
const val typeGameRC = "RC"
const val typePlay = "play"
const val typeHit = "hit"

