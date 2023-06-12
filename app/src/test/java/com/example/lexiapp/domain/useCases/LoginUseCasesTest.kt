package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.AuthenticationServiceImpl
import com.example.lexiapp.domain.model.LoginResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoginUseCasesTest {

    @RelaxedMockK
    private lateinit var mAuth: AuthenticationServiceImpl

    lateinit var loginUseCases: LoginUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        loginUseCases = LoginUseCases(mAuth)
    }

    @Test
    fun `given a valid email address to let me log in`() = runBlocking {
        //Given
        val email = "email@fake.com"
        val password = "123456"
        coEvery { mAuth.login(email, password) } returns LoginResult.Success
        //When
        val result = loginUseCases(email, password)
        //Then
        assert(result.equals(LoginResult.Success))
    }

    @Test
    fun `given an invalid email will not let me log in`() = runBlocking {
        //Given
        val email = "emailfake.com"
        val password = "123456"
        coEvery { mAuth.login(email, password) } returns LoginResult.Success
        //When
        val result = loginUseCases(email, password)
        //Then
        assert(result.equals(LoginResult.Error))
    }

    @Test
    fun `that given a too short password of an error`()= runBlocking {
        //Given
        val email = "email@fake.com"
        val password = "123"
        coEvery { mAuth.login(email, password) } returns LoginResult.Success
        val result = loginUseCases(email, password)
        assert(result.equals(LoginResult.Error))
    }
}