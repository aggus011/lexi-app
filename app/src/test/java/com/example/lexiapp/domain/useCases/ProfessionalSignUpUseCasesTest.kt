package com.example.lexiapp.domain.useCases

import android.content.SharedPreferences
import com.example.lexiapp.data.network.AuthenticationServiceImpl
import com.example.lexiapp.data.network.FireStoreServiceImpl
import com.example.lexiapp.domain.model.LoginResult
import com.example.lexiapp.domain.model.ProfessionalSignUp
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ProfessionalSignUpUseCasesTest {

    @RelaxedMockK
    private lateinit var authenticationServiceImpl: AuthenticationServiceImpl
    @RelaxedMockK
    private lateinit var firestoreServiceImpl: FireStoreServiceImpl
    @RelaxedMockK
    private lateinit var sharedPrefs: SharedPreferences

    lateinit var professionalSignUpUseCases: ProfessionalSignUpUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        professionalSignUpUseCases = ProfessionalSignUpUseCases(authenticationServiceImpl, firestoreServiceImpl, sharedPrefs)
    }

    @Test
    fun `when try to create a new Profesional and te info is valid, return Success`() = runBlocking {
        //Given
        val professional = ProfessionalSignUp(name, surname, medicalRegistration, validEmail, validEmail, validPassword, validPassword)
        coEvery { authenticationServiceImpl.createAccount(validEmail, validPassword) } returns LoginResult.Success
        //When
        val result = professionalSignUpUseCases(professional)
        //Then
        assert(result == LoginResult.Success)
    }

    @Test
    fun `when try to create a new Profesional and te info is valid but can not create it, return Error`() = runBlocking {
        //Given
        val professional = ProfessionalSignUp(name, surname, medicalRegistration, validEmail, validEmail, validPassword, validPassword)
        coEvery { authenticationServiceImpl.createAccount(validEmail, validPassword) } returns LoginResult.Error
        //When
        val result = professionalSignUpUseCases(professional)
        //Then
        assert(result == LoginResult.Error)
    }

    @Test
    fun `when try to create a new Profesional but the email is not valid, return Error`() = runBlocking {
        //Given
        val professional = ProfessionalSignUp(name, surname, medicalRegistration, invalidEmail, invalidEmail, validPassword, validPassword)
        coEvery { authenticationServiceImpl.createAccount(validEmail, validPassword) } returns LoginResult.Error
        //When
        val result = professionalSignUpUseCases(professional)
        //Then
        assert(result == LoginResult.Error)
    }

    @Test
    fun `when try to create a new Profesional but the emails not match, return Error`() = runBlocking {
        //Given
        val professional = ProfessionalSignUp(name, surname, medicalRegistration, validEmail, notMatchEmail, validPassword, validPassword)
        coEvery { authenticationServiceImpl.createAccount(validEmail, validPassword) } returns LoginResult.Error
        //When
        val result = professionalSignUpUseCases(professional)
        //Then
        assert(result == LoginResult.Error)
    }

    @Test
    fun `when try to create a new Profesional but the passwords not match, return Error`() = runBlocking {
        //Given
        val professional = ProfessionalSignUp(name, surname, medicalRegistration, validEmail, validEmail, validPassword, notMatchPassword)
        coEvery { authenticationServiceImpl.createAccount(validEmail, validPassword) } returns LoginResult.Error
        //When
        val result = professionalSignUpUseCases(professional)
        //Then
        assert(result == LoginResult.Error)
    }

    @Test
    fun `when try to create a new Profesional but the password is not valid, return Error`() = runBlocking {
        //Given
        val professional = ProfessionalSignUp(name, surname, medicalRegistration, validEmail, validEmail, invalidPassword, invalidPassword)
        coEvery { authenticationServiceImpl.createAccount(validEmail, validPassword) } returns LoginResult.Error
        //When
        val result = professionalSignUpUseCases(professional)
        //Then
        assert(result == LoginResult.Error)
    }

}

const val validEmail = "test@gmail.com"
const val notMatchEmail = "t$validEmail"
const val invalidEmail = "testgmail.com"
const val validPassword ="myPassword01"
const val notMatchPassword = "m$validPassword"
const val invalidPassword = "123"
const val name = "Name"
const val surname = "Lastname"
const val medicalRegistration = "1111"