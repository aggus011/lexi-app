package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.R
import com.example.lexiapp.domain.service.FireStoreService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

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



}

const val justName = "Name"
const val completedName = "Name Lastname"
