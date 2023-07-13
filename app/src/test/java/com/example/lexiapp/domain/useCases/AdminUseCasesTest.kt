package com.example.lexiapp.domain.useCases

import com.example.lexiapp.data.network.AdminServiceImpl
import com.example.lexiapp.domain.model.ProfessionalValidation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class AdminUseCasesTest{
    @RelaxedMockK
    private lateinit var repository: AdminServiceImpl

    lateinit var adminUseCases: AdminUseCases

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        coEvery{ repository.getRegisteredProfessionals() } returns flowOf(listOf(ProfessionalValidation("Name","test@gmail.com", "1111", false)))
        adminUseCases = AdminUseCases(repository)
    }

    @Test
    fun `get all professional registed in the app` () = runBlocking{
        //Given
        //When - Then
        adminUseCases.getRegisteredProfessionals().collect{
            assert(it.size==1)
            assert(it[0].name == "Name")
            assert(it[0].email == "test@gmail.com")
            assert(it[0].medicalRegistration == "1111")
            assert(!it[0].validated)
        }
    }

    @Test
    fun `when set approval, that the admin service set approval`() = runBlocking {
        //Given
        val professionalsValidation = "test@gmail.com"
        val approval = true
        //When
        adminUseCases.saveApprovalToProfessional(professionalsValidation, approval)
        //Then
        coVerify (exactly = 1) { repository.saveApprovalToProfessional(professionalsValidation, approval) }
    }
}