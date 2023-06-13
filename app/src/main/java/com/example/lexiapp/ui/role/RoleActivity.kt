package com.example.lexiapp.ui.role

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.lexiapp.databinding.ActivityRoleBinding
import com.example.lexiapp.ui.signup.patient.SignUpActivity
import com.example.lexiapp.ui.signup.professional.ProfessionalSignUpActivity
import com.google.android.material.button.MaterialButton

class RoleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRoleBinding
    private lateinit var btnSignUpPatient: MaterialButton
    private lateinit var btnSignUpProfessional: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoleBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getViews()
        setListeners()
    }

    private fun getViews() {
        btnSignUpPatient = binding.mbPatientRole
        btnSignUpProfessional = binding.mbProfessionalRole
    }

    private fun setListeners(){
        goToPatientSignUp()
        goToProfessionalSignUp()
    }

    private fun goToPatientSignUp() {
        btnSignUpPatient.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun goToProfessionalSignUp(){
        btnSignUpProfessional.setOnClickListener {
            startActivity(Intent(this, ProfessionalSignUpActivity::class.java))
        }
    }
}