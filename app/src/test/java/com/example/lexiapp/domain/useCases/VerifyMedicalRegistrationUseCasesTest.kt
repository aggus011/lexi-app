package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.domain.model.Professional
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.service.FireStoreService
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class VerifyMedicalRegistrationUseCasesTest {
    @RelaxedMockK
    private lateinit var sharedPrefs: SharedPreferences

    @RelaxedMockK
    private lateinit var firestoreService: FireStoreService

    private lateinit var verifyMedicalRegistrationUseCases: VerifyMedicalRegistrationUseCases

    @Before
    fun setup() {
        sharedPrefs = mockk(relaxed = true)
        firestoreService = mockk(relaxed = true)
        verifyMedicalRegistrationUseCases = VerifyMedicalRegistrationUseCases(sharedPrefs, firestoreService)
    }


    @Test
    fun `isBeenTwoDaysSinceRegistration returns true if two days have passed since registration`() {
        // Given
        val registrationDate = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3))
        val currentDate = Date()

        // When
        val result = verifyMedicalRegistrationUseCases.isBeenTwoDaysSinceRegistration(registrationDate, currentDate)

        // Then
        assertEquals(true, result)
    }

    @Test
    fun `isBeenTwoDaysSinceRegistration returns false if less than two days have passed since registration`() {
        // Given
        val registrationDate = Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1))
        val currentDate = Date()

        // When
        val result = verifyMedicalRegistrationUseCases.isBeenTwoDaysSinceRegistration(registrationDate, currentDate)

        // Then
        assertEquals(false, result)
    }


    @Test
    fun `getCurrentUserEmail returns the current user's email`() {
        // Given
        val email = "test@example.com"
        coEvery { sharedPrefs.getString("email", null) } returns email

        // When
        val result = verifyMedicalRegistrationUseCases.getCurrentUserEmail()

        // Then
        assertEquals(email, result)
    }


    @Test
    fun `getProfessionalAccountState returns professional account state and registration date`() = runBlocking {
        // Given
        val email = "test@example.com"
        val isVerifiedAccount = true
        val registrationDate = Date()
        val professional = Professional(user = null, medicalRegistration = null, patients = null, isVerifiedAccount = isVerifiedAccount, registrationDate = registrationDate)
        val firestoreService = mockk<FireStoreService>()
        coEvery { firestoreService.getProfessional(email) } returns professional

        val sharedPrefs = mockk<SharedPreferences>()
        val editor = mockk<SharedPreferences.Editor>()

        every { sharedPrefs.edit() } returns editor
        every { editor.apply() } just runs

        val verifyMedicalRegistrationUseCases = VerifyMedicalRegistrationUseCases(
            sharedPrefs = sharedPrefs,
            firestoreServiceImpl = firestoreService
        )
        // When
        val result = verifyMedicalRegistrationUseCases.getProfessionalAccountState(email)
        // Then
        assertEquals(isVerifiedAccount, result.first)
        assertEquals(registrationDate, result.second)
    }

    @Test
    fun `getProfessionalAccountState returns professional account state false`() = runBlocking {
        // Given
        val email = "test@example.com"
        val isVerifiedAccount = false // Cambio a falso
        val registrationDate = Date()
        val professional = Professional(
            user = null,
            medicalRegistration = null,
            patients = null,
            isVerifiedAccount = isVerifiedAccount,
            registrationDate = registrationDate
        )
        val firestoreService = mockk<FireStoreService>()
        coEvery { firestoreService.getProfessional(email) } returns professional

        val sharedPrefs = mockk<SharedPreferences>()
        val editor = mockk<SharedPreferences.Editor>()

        every { sharedPrefs.edit() } returns editor
        every { editor.apply() } just runs

        val verifyMedicalRegistrationUseCases = VerifyMedicalRegistrationUseCases(
            sharedPrefs = sharedPrefs,
            firestoreServiceImpl = firestoreService
        )
        // When
        val result = verifyMedicalRegistrationUseCases.getProfessionalAccountState(email)
        // Then
        assertEquals(isVerifiedAccount, result.first)
        assertEquals(registrationDate, result.second)
    }


    }




