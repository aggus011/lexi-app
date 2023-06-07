package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.AuthenticationService
import com.example.lexiapp.data.response.LoginResult
import com.example.lexiapp.domain.model.UserSignUp
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignUpUseCasesTest {

    @RelaxedMockK
    private lateinit var mAuth: AuthenticationService

    lateinit var signUpUseCases: SignUpUseCases

    private lateinit var profileUseCases: ProfileUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        signUpUseCases = SignUpUseCases(mAuth)
    }

    @Test
    fun `with empty fields do not allow to create the user`() = runBlocking {
        //Given
        val name = ""
        val email = ""
        val password = ""
        coEvery { mAuth.createAccount(email, password) } returns LoginResult.Success
        //When
        val result = signUpUseCases(UserSignUp(name, email, email, password, password))
        //Then
        assert(result == LoginResult.Error)
    }

    @Test
    fun `by entering an invalid main it will not let me create the user`()  = runBlocking {
        //Given
        val name = "asdfasdfasd"
        val email = "asdjkfhadsj"
        val password = "sdfasdsfsad"
        coEvery { mAuth.createAccount(email, password) } returns LoginResult.Success
        //When
        val result = signUpUseCases(UserSignUp(name, email, email, password, password))
        //Then
        assert(LoginResult.Error == result)
    }

}