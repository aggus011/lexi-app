package com.example.lexiapp.domain.useCases

import com.example.lexiapp.domain.model.ProfessionalValidation
import com.example.lexiapp.domain.service.AdminService
import javax.inject.Inject

class AdminUseCases @Inject constructor(
    private val adminService: AdminService
) {

    suspend fun getRegisteredProfessionals() =
        adminService.getRegisteredProfessionals()

    suspend fun saveApprovalToProfessional(emailProfessional: String, approval: Boolean) =
        adminService.saveApprovalToProfessional(emailProfessional, approval)

}