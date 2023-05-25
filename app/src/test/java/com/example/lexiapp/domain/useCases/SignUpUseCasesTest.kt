package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.AuthenticationService
import com.example.lexiapp.data.response.LoginResult
import com.example.lexiapp.domain.model.UserSignUp
import com.google.firebase.auth.AuthResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignUpUseCasesTest {

    @RelaxedMockK
    private lateinit var mAuth: AuthenticationService

    lateinit var signUpUseCases: SignUpUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        signUpUseCases = SignUpUseCases(mAuth)
    }

    @Test
    fun `que con campos vacíos no deje crear el usuario`() = runBlocking {
        //Given
        val name = ""
        val email = ""
        val password = ""
        coEvery { mAuth.createAccount(email, password) } returns null
        //When
        val result = signUpUseCases(UserSignUp(name, email, password))
        //Then
        assertFalse(result)
    }

}