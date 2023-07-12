package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.data.network.AuthenticationServiceImpl
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.model.ProfessionalSignUp
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
    fun `by entering an invalid mail it will not let me create the user and return EmailInvalid`()  = runBlocking {
        //Given
        val name = "Name"
        val email = "invalidemail.com"
        val password = "my0password"
        //When
        val result = signUpUseCases(UserSignUp(name, email, email, password, password))
        //Then
        assert(result == LoginResult.EmailInvalid)
    }

    @Test
    fun `when the emails not match, return DistinctEmail`() = runBlocking {
        //Given
        val name = "Name"
        val email = "valid@email.com"
        val confirmEmail = "invalidemail.com"
        val password = "my0password"
        //When
        val result = signUpUseCases(UserSignUp(name, email, confirmEmail, password, password))
        //Then
        assert(result == LoginResult.DistinctEmail)
    }

    @Test
    fun `when the passwords not match, return DistinctPassword`() = runBlocking {
        //Given
        val name = "Name"
        val email = "valid@email.com"
        val password = "my0password"
        val confirmPassword = "cant"
        //When
        val result = signUpUseCases(UserSignUp(name, email, email, password, confirmPassword))
        //Then
        assert(result == LoginResult.DistinctPassword)
    }

    @Test
    fun `when the the password is not valid, return PasswordInvalid`() = runBlocking {
        //Given
        val name = "Name"
        val email = "valid@email.com"
        val password = "cant"
        //When
        val result = signUpUseCases(UserSignUp(name, email, email, password, password))
        //Then
        assert(result == LoginResult.PasswordInvalid)
    }

}