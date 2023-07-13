package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.R
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.model.UserLogin
import com.example.lexiapp.domain.service.FireStoreService
import io.mockk.*
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import java.util.*

class ProfileUseCasesTest{
    @RelaxedMockK
    private lateinit var prefs: SharedPreferences
    @RelaxedMockK
    private lateinit var fireStoreService: FireStoreService

    lateinit var profileUseCases: ProfileUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        profileUseCases = ProfileUseCases(prefs, fireStoreService)
    }

    @Test
    fun `when the name has one word, return just the first letter`() = runBlocking {
        //Given
        every{ prefs.getString("user_name", null) } returns justName
        //When
        val inits = profileUseCases.userInitials()
        //Then
        assert(inits == "N")
    }

    @Test
    fun `when the name has two words, return the first letter of each`() = runBlocking {
        //Given
        every{ prefs.getString("user_name", null) } returns completedName
        //When
        val inits = profileUseCases.userInitials()
        //Then
        assert(inits == "NL")
    }


    @Test
    fun `when getting the professional verification state, it should return the saved verification state from SharedPreferences`() {
        // Given
        val verificationState = 2

        every { prefs.getInt("professional_account_state", 0) } returns verificationState

        // When
        val result = profileUseCases.getProfessionalVerificationState()

        // Then
        assertEquals(result, verificationState)
    }


    @Test
    fun `when getting the user type, it should return the saved user type from SharedPreferences`() {
        // Given
        val userType = "profesional"

        every { prefs.getString("user_type", null) } returns userType

        // When
        val result = profileUseCases.getUserType()

        // Then
        assertEquals(result, userType)
    }

    @Test
    fun `when editing a profile, it should save the modified user in Firestore`() = runBlocking {
        // Given
        val userName = completedName
        val professional = "12345"
        val birthDate = null

        val modifiedUser = User(email = "asd@asd.com", userName = completedName, birthDate = null, profesional = professional)

        coEvery { prefs.getString("user_name", null) } returns userName

        // When
        val result = profileUseCases.editProfile(completedName, professional, birthDate)

        // Then
        assertEquals(modifiedUser.userName, result.userName)
        coVerify { fireStoreService.saveAccount(result) }
    }

    @Test
    fun `when checking if there is an account, it should return false if email is null`() {
        // Given
        every { prefs.getString("email", null) } returns null

        // When
        val result = profileUseCases.haveAccount()

        // Then
        assert(!result)
    }

    @Test
    fun `when checking if there is an account, it should return true if email is not null`() {
        // Given
        every { prefs.getString("email", null) } returns "test@example.com"

        // When
        val result = profileUseCases.haveAccount()

        // Then
        assert(result)
    }



}

const val justName = "Name"
const val completedName = "Name Lastname"
