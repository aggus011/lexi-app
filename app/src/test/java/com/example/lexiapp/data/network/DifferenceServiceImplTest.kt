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
        coEvery { db.getDeviceToken() } returns deviceToken
        differenceService = DifferenceServiceImpl(apiDifferenceGateway, db, notificationServiceImpl)
    }

    @Test
    fun `when try to save a positive result, save the result and update the play and hit progress`() = runBlocking {
        // Given
        val result = LetsReadGameResult(
            email = emailTest_DIFF,
            success = positiveSuccess_DIFF,
            wrongWords = wrongWords,
            totalWords = totalWords_DIFF,
            date = date_DIFF
        )
        // When
        differenceService.saveLetsReadResult(result, isNotChallenge_DIFF)
        // Then
        coVerify(exactly = 1) { db.saveLetsReadResult(result.toLetsReadGameDataResult()) }
        coVerify(exactly = 1) { db.updateObjectiveProgress("LR", "play") }
        coVerify(exactly = 1) { db.updateObjectiveProgress("LR", "hit") }
    }

    @Test
    fun `when try to save a negative result, save the result and update the play progress`() = runBlocking {
        // Given
        val result = LetsReadGameResult(
            email = emailTest_DIFF,
            success = negativeSuccess_DIFF,
            wrongWords = wrongWords,
            totalWords = totalWords_DIFF,
            date = date_DIFF
        )
        // When
        differenceService.saveLetsReadResult(result, isNotChallenge_DIFF)
        // Then
        coVerify(exactly = 1) { db.saveLetsReadResult(result.toLetsReadGameDataResult()) }
        coVerify(exactly = 1) { db.updateObjectiveProgress("LR", "play") }
        coVerify(exactly = 0) { db.updateObjectiveProgress("LR", "hit") }
    }

    @Test
    fun `when try to save a positive challengeResult, save the result and update the play and hit progress`() = runBlocking {
        // Given
        val result = LetsReadGameResult(
            email = emailTest_DIFF,
            success = positiveSuccess_DIFF,
            wrongWords = wrongWords,
            totalWords = totalWords_DIFF,
            date = date_DIFF
        )
        // When
        differenceService.saveLetsReadResult(result, isChallenge_DIFF)
        // Then
        coVerify(exactly = 1) { db.saveLetsReadResult(result.toLetsReadGameDataResult()) }
        coVerify(exactly = 1) { db.updateObjectiveProgress("RC", "play") }
        coVerify(exactly = 1) { db.updateObjectiveProgress("RC", "hit") }
    }

    @Test
    fun `when try to save a negative challengeResult, save the result and update the play and hit progress`() = runBlocking {
        // Given
        val result = LetsReadGameResult(
            email = emailTest_DIFF,
            success = negativeSuccess_DIFF,
            wrongWords = wrongWords,
            totalWords = totalWords_DIFF,
            date = date_DIFF
        )
        // When
        differenceService.saveLetsReadResult(result, isChallenge_DIFF)
        // Then
        coVerify(exactly = 1) { db.saveLetsReadResult(result.toLetsReadGameDataResult()) }
        coVerify(exactly = 1) { db.updateObjectiveProgress("RC", "play") }
        coVerify(exactly = 0) { db.updateObjectiveProgress("RC", "hit") }
    }

    @Test
    fun `when an objectivePlay is complete, send the notification of that objective`() = runBlocking {
        // Given
        coEvery { db.checkIfObjectiveAndWeeklyObjectivesWereCompleted(typeGameLR_DIFF, typePlay_DIFF) } returns objectiveComplete_DIFF
        // When
        differenceService.generateNotificationIfObjectiveHasBeenCompleted(typeGameLR_DIFF, typePlay_DIFF, nameGame)
        // Then
        coVerify(exactly = 1) { notificationServiceImpl.sendNotificationObjectiveCompleted(deviceToken, "Jugar $nameGame") }
        coVerify(exactly = 0) { notificationServiceImpl.sendNotificationWeeklyObjectivesCompleted(deviceToken) }
    }

    @Test
    fun `when an objectiveHit is complete, send the notification of that objective`() = runBlocking {
        // Given
        coEvery { db.checkIfObjectiveAndWeeklyObjectivesWereCompleted(typeGameRC_DIFF, typeHit_DIFF) } returns objectiveComplete_DIFF
        // When
        differenceService.generateNotificationIfObjectiveHasBeenCompleted(typeGameRC_DIFF, typeHit_DIFF, nameGame)
        // Then
        coVerify(exactly = 1) { notificationServiceImpl.sendNotificationObjectiveCompleted(deviceToken, "Acertar en $nameGame") }
        coVerify(exactly = 0) { notificationServiceImpl.sendNotificationWeeklyObjectivesCompleted(deviceToken) }
    }

    @Test
    fun `when all objectives of week are complete, send the weekly notification`() = runBlocking {
        // Given
        coEvery { db.checkIfObjectiveAndWeeklyObjectivesWereCompleted(typeGameRC_DIFF, typeHit_DIFF) } returns allObjectivesComplete_DIFF
        coEvery { db.getDeviceToken() } returns deviceToken
        // When
        differenceService.generateNotificationIfObjectiveHasBeenCompleted(typeGameRC_DIFF, typeHit_DIFF, nameGame)
        // Then
        coVerify(exactly = 0) { notificationServiceImpl.sendNotificationObjectiveCompleted(deviceToken, "Acertar $nameGame") }
        coVerify(exactly = 1) { notificationServiceImpl.sendNotificationWeeklyObjectivesCompleted(deviceToken) }
    }
}

const val emailTest_DIFF: String = "test@gmail.com"
const val positiveSuccess_DIFF: Boolean = true
const val negativeSuccess_DIFF: Boolean = false
const val isChallenge_DIFF: Boolean = true
const val isNotChallenge_DIFF: Boolean = false
val wrongWords: List<String> = listOf("CASA", "PATO", "BOTA")
const val totalWords_DIFF: Int = 20
val date_DIFF: String = System.currentTimeMillis().toString()
const val deviceToken: String = "e3eHzqEbRo2JsonyA5dapb:A"
val objectiveComplete_DIFF: Pair<Boolean, Boolean> = Pair(true, false)
val allObjectivesComplete_DIFF: Pair<Boolean, Boolean> = Pair(true, true)
const val nameGame: String = "Vamos a Leer"
const val typeGameLR_DIFF: String = "LR"
const val typeGameRC_DIFF: String = "RC"
const val typePlay_DIFF: String = "play"
const val typeHit_DIFF: String = "hit"
