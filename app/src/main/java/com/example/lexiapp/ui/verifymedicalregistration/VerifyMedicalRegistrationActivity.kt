package com.example.lexiapp.ui.verifymedicalregistration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityVerifyMedicalRegistrationBinding
import com.example.lexiapp.ui.profesionalhome.ProfesionalHomeActivity
import com.example.lexiapp.ui.signup.professional.ProfessionalSignUpActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyMedicalRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifyMedicalRegistrationBinding
    private val viewModel: VerifyMedicalRegistrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyMedicalRegistrationBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setObservers()
        getLastUpdateVerification()
        setListeners()
    }

    private fun setObservers(){
        viewModel.verificationState.observe(this){
            if(it){
              binding.clValidatedAccount.visibility = View.VISIBLE
              binding.tvValidation.visibility = View.GONE
              binding.clNoValidatedAccount.visibility = View.GONE
              binding.mbUpdate.visibility = View.GONE
            }
        }
        viewModel.errorVerification.observe(this){
            if(it){
                binding.clValidatedAccount.visibility = View.GONE
                binding.tvValidation.visibility = View.GONE
                binding.clNoValidatedAccount.visibility = View.VISIBLE
                binding.mbUpdate.visibility = View.GONE
            }
        }
    }

    private fun getLastUpdateVerification(){
        viewModel.getLastUpdateAccountVerification()
    }

    private fun setListeners() {
        binding.mbReviewRegistrationData.setOnClickListener {
            viewModel.changeProfessionalAccountStateToNotBeenVerified()
            startActivity(Intent(this, ProfessionalSignUpActivity::class.java))
            finish()
        }
        binding.mbContinueToHome.setOnClickListener {
            viewModel.changeProfessionalAccountStateToNotBeenVerified()
            startActivity(Intent(this, ProfesionalHomeActivity::class.java))
            finish()
        }
        binding.mbUpdate.setOnClickListener {
            getLastUpdateVerification()
        }
    }
}