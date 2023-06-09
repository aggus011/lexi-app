package com.example.lexiapp.data.repository.patient

import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.model.Patient

object PatientMocks {
    fun getPatientMocks(): List<Patient> {

        val patient1 = Patient.Builder()
            .user("aaaa@gmail.com", "Pablo")
            .build()
        val patient2 = Patient.Builder()
            .user("bbbb@gmail.com","Blanca")
            .build()
        val patient3 = Patient.Builder()
            .user("cccc@gmail.com","Camilo")
            .build()
        val patient4 = Patient.Builder()
            .user("dddd@gmail.com","Dana")
            .build()
        val patient5 = Patient.Builder()
            .user("eeee@gmail.com","Emiliano" )
            .build()

        return listOf(patient1,patient2,patient3,patient4,patient5)
    }
}