package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.service.FireStoreService
import com.example.lexiapp.domain.service.FirebaseNotificationService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LinkUseCasesTest{
    @RelaxedMockK
    private lateinit var firestoreService: FireStoreService
    @RelaxedMockK
    private lateinit var sharedPrefs: SharedPreferences
    @RelaxedMockK
    private lateinit var firebaseNotificationService: FirebaseNotificationService

    lateinit var linkUseCases: LinkUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        every{ sharedPrefs.getString("email", null) } returns emailTest
        linkUseCases = LinkUseCases(firestoreService, sharedPrefs, firebaseNotificationService)
    }

    @Test
    fun `when try to bind a professional to player and it is success, return TaskSuccess`() = runBlocking{
        //Given
        coEvery { firestoreService.getProfessionalToken(emailTest) } returns tokenTest
        coEvery { firestoreService.getPatientToken(emailPlayerTest) } returns tokenPlayerTest
        coEvery { firestoreService.bindProfessionalToPatient(emailPlayerTest, emailTest) } returns FirebaseResult.TaskSuccess
        //When - Then
        linkUseCases.bindProfessionalToPatient(emailPlayerTest).collect{
            assert(it == FirebaseResult.TaskSuccess)
        }
    }

    @Test
    fun `when try to bind a professional to player and it's not success, return TaskFailure`() = runBlocking{
        //Given
        coEvery { firestoreService.getProfessionalToken(emailTest) } returns tokenTest
        coEvery { firestoreService.getPatientToken(emailPlayerTest) } returns tokenPlayerTest
        coEvery { firestoreService.bindProfessionalToPatient(emailPlayerTest, emailTest) } returns FirebaseResult.TaskFailure
        //When - Then
        linkUseCases.bindProfessionalToPatient(emailPlayerTest).collect{
            assert(it == FirebaseResult.TaskFailure)
        }
    }

    @Test
    fun `when try to bind but the tokens are null, the bind still made but without notification`() = runBlocking{
        //Given
        coEvery { firestoreService.getProfessionalToken(emailTest) } returns null
        coEvery { firestoreService.getPatientToken(emailPlayerTest) } returns null
        coEvery { firestoreService.bindProfessionalToPatient(emailPlayerTest, emailTest) } returns FirebaseResult.TaskSuccess
        //When - Then
        linkUseCases.bindProfessionalToPatient(emailPlayerTest).collect{
            assert(it == FirebaseResult.TaskSuccess)
        }
        coVerify (exactly = 0) { firebaseNotificationService.sendNotificationToPatient(tokenPlayerTest,emailPlayerTest) }
        coVerify (exactly = 0) { firebaseNotificationService.sendNotificationToProfessional(tokenTest,emailTest) }
    }

    @Test
    fun `when try to add a player to professional and it is success, return TaskSuccess `() = runBlocking{
        //Given
        coEvery { firestoreService.addPatientToProfessional(emailPlayerTest, emailTest) } returns CompletableDeferred(FirebaseResult.TaskSuccess)
        //When - Then
        linkUseCases.addPatientToProfessional(emailPlayerTest).collect{
            assert(it == FirebaseResult.TaskSuccess)
        }
    }

    @Test
    fun `when try to add a player to professional and it's not success, return TaskFailure `() = runBlocking{
        //Given
        coEvery { firestoreService.addPatientToProfessional(emailPlayerTest, emailTest) } returns CompletableDeferred(FirebaseResult.TaskFailure)
        //When - Then
        linkUseCases.addPatientToProfessional(emailPlayerTest).collect{
            assert(it == FirebaseResult.TaskFailure)
        }
    }

    @Test
    fun `when try get players binds, return a list of strings`() = runBlocking{
        //Given
        coEvery { firestoreService.getListLinkPatientOfProfessional(emailTest) } returns flowOf(listPlayers)
        //When - Then
        linkUseCases.getListLinkPatientOfProfessional().collect{
            assert(it == listPlayers)
        }
    }

    @Test
    fun `when try to get a players with email and it exists, return a User`() = runBlocking{
        //Given
        coEvery { firestoreService.getUser(emailPlayerTest) } returns playerTest
        //When
        val player = linkUseCases.getUser(emailPlayerTest)
        //Then
        assert(player.email == emailPlayerTest)
    }

}

const val emailTest = "test@gmail.com"
const val tokenTest = "e3eHzqEbRo2JsonyA5dapb:A"
const val tokenPlayerTest = "zGzuKA95YKhtLyAxsMpocaq3jD1Rz"
const val emailPlayerTest = "test.player@gmail.com"
val listPlayers = listOf("test.player1@gmail.com","test.player2@gmail.com","test.player3@gmail.com")
val playerTest = User("Name", emailPlayerTest, profesional = emailTest)