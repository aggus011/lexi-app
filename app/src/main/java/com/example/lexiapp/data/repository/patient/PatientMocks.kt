package com.example.lexiapp.data.repository.patient

import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.model.Patient

object PatientMocks {
    fun getPatientMocks(): List<Patient> {

        val patient1 = Patient.Builder()
            .email("aaaa@gmail.com")
            .name("Pablo")
            .build()
        val patient2 = Patient.Builder()
            .email("bbbb@gmail.com")
            .name("Blanca")
            .build()
        val patient3 = Patient.Builder()
            .email("cccc@gmail.com")
            .name("Camilo")
            .build()
        val patient4 = Patient.Builder()
            .email("dddd@gmail.com")
            .name("Dana")
            .build()
        val patient5 = Patient.Builder()
            .email("eeee@gmail.com")
            .name("Emiliano")
            .build()

        return listOf(patient1,patient2,patient3,patient4,patient5)
    }
}