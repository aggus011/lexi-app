package com.example.lexiapp.data.network

import com.example.lexiapp.domain.model.ProfessionalValidation
import com.example.lexiapp.domain.service.AdminService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdminServiceImpl @Inject constructor(
    private val db: FireStoreServiceImpl
): AdminService {

    override suspend fun getRegisteredProfessionals() =
        db.getAllProfessional()

    override suspend fun saveApprovalToProfessional(emailProfessional: String, approval: Boolean) =
        db.saveValidationTOProfessional(emailProfessional, approval)

}