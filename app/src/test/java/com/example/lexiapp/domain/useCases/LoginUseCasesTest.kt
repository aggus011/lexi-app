package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.AuthenticationService
import com.example.lexiapp.data.response.LoginResult
import com.google.firebase.auth.FirebaseAuth
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class LoginUseCasesTest {

    @RelaxedMockK
    private lateinit var mAuth: AuthenticationService

    lateinit var loginUseCases: LoginUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        loginUseCases = LoginUseCases(mAuth)
    }

    @Test
    fun `que dado un email valido me deje iniciar sesion`() = runBlocking {
        //Given
        val email = "email@fake.com"
        val password = "123456"
        coEvery { mAuth.login(email, password) } returns LoginResult.Success
        val result = loginUseCases(email, password)
        assert(result.equals(LoginResult.Success))
    }

    @Test
    fun `que dado un email invalido no me deje iniciar sesion`() = runBlocking {
        //Given
        val email = "emailfake.com"
        val password = "123456"
        coEvery { mAuth.login(email, password) } returns LoginResult.Success
        val result = loginUseCases(email, password)
        assert(result.equals(LoginResult.Error))
    }
}