package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.TextScannerService
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TextScannerUseCasesTest {

    @RelaxedMockK
    private lateinit var firestore: FireStoreService
    @RelaxedMockK
    private lateinit var prefs: SharedPreferences
    @RelaxedMockK
    private lateinit var service: TextScannerService

    lateinit var textScannerUseCases: TextScannerUseCases
    lateinit var userMail: String

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        every{ prefs.getString("email", null) } returns email
        userMail = prefs.getString("email", null)!!
        textScannerUseCases = TextScannerUseCases(firestore, prefs, service)
    }

    @Test
    fun `when try to save a resultTS, save the result and update progress`() = runBlocking{
        //When
        textScannerUseCases.saveTSResult()
        //Then
        coVerify (exactly = 1) { firestore.saveTextScanResult(userMail) }
        coVerify (exactly = 1) { firestore.updateObjectiveProgress(game, type) }
    }

    @Test
    fun `when generate a notification, call the service to generate it`() = runBlocking{
        //When
        textScannerUseCases.generateNotificationForObjectives(game, type, nameGame)
        //Then
        coVerify (exactly = 1) { service.generateNotificationIfObjectiveHasBeenCompleted(game, type, nameGame) }
    }
}

const val email = "test@gmail.com"
const val game = "SCAN"
const val type = "play"
const val nameGame = "Escanéo de Texto"