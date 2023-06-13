package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.data.network.AuthenticationServiceImpl
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.domain.service.FireStoreService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SignUpUseCasesTest {

    @RelaxedMockK
    private lateinit var mAuth: AuthenticationServiceImpl
    @RelaxedMockK
    private lateinit var firestore: FireStoreService
    @RelaxedMockK
    private lateinit var sharedPreferences: SharedPreferences

    lateinit var signUpUseCases: SignUpUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        signUpUseCases = SignUpUseCases(mAuth, firestore, sharedPreferences)
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