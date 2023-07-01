package com.example.lexiapp.domain.service

import com.example.lexiapp.domain.model.ProfessionalValidation
import kotlinx.coroutines.flow.Flow

interface AdminService {

    suspend fun getRegisteredProfessionals(): Flow<List<ProfessionalValidation>>

    suspend fun saveApprovalToProfessional(emailProfessional: String, approval: Boolean)
}