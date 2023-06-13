package com.example.lexiapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.lexiapp.databinding.ActivityMainBinding
import com.example.lexiapp.domain.useCases.ProfileUseCases
import com.example.lexiapp.ui.login.LoginActivity
import com.example.lexiapp.ui.patienthome.HomePatientActivity
import com.example.lexiapp.ui.profesionalhome.ProfesionalHomeActivity
import com.example.lexiapp.ui.verifymedicalregistration.VerifyMedicalRegistrationActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var profileUseCases: ProfileUseCases

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //To force app to only light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        verifyCurrentSession()
    }

    private fun verifyCurrentSession(){
        if(profileUseCases.haveAccount()){
            val userType = profileUseCases.getUserType()
            if(userType == "patient"){
                startActivity(Intent(this, HomePatientActivity::class.java))
            }else{
                val professionalState = profileUseCases.getProfessionalVerificationState()
                if(professionalState == 1){
                    startActivity(Intent(this, VerifyMedicalRegistrationActivity::class.java))
                }else if(professionalState == 2){
                    startActivity(Intent(this, ProfesionalHomeActivity::class.java))
                }
            }
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}