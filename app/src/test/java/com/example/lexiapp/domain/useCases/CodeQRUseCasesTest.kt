package com.example.lexiapp.domain.useCases

import io.mockk.MockKAnnotations
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class CodeQRUseCasesTest{
    private lateinit var codeQRUseCases: CodeQRUseCases

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        codeQRUseCases = CodeQRUseCases()
    }

    @Test
    fun `when have the content of QR, getEmailFormQR should return the email`() {
        //Given
        val code = "LEXIAPP2023".hashCode().toString()
        val emailSent = "test@gmail.com"
        val contents = code + emailSent
        //When
        val email = codeQRUseCases.getEmailFromQR(contents)
        //Then
        assertEquals(emailSent, email)
    }

    @Test
    fun `getEmailFromQR should return null when contents isn't verify`() {
        //Given
        val contents = "test@gmail.com"
        //When
        val email = codeQRUseCases.getEmailFromQR(contents)
        //Then
        assertEquals(null, email)
    }

    @Test
    fun `when the content is too small, throw a StringIndexOutOfBoundsException`() {
        //Given
        val contents = ""
        //When - Then
        try {
            codeQRUseCases.getEmailFromQR(contents)
        } catch (e: Exception) {
            assertEquals(e::class, StringIndexOutOfBoundsException::class)
        }
    }
}