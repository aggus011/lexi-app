package com.example.lexiapp.ui.verifymedicalregistration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

    private fun setObservers() {
        viewModel.verificationState.observe(this) {
            if (it) {
                showValidatedAccountState()
                hideProgressCircular()
            } else{
                showValidationAccountState()
                hideProgressCircular()
            }
        }
        viewModel.errorVerification.observe(this) {
            if (it) {
                showNoValidatedAccountState()
                hideProgressCircular()
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
            showProgressCircular()
            hideValidationAccountState()
            getLastUpdateVerification()
        }
    }

    private fun showProgressCircular() {
        binding.progressCircularValidation.visibility = View.VISIBLE
    }

    private fun hideProgressCircular() {
        binding.progressCircularValidation.visibility = View.GONE
    }

    private fun showValidatedAccountState() {
        binding.clValidatedAccount.visibility = View.VISIBLE
    }

    private fun showValidationAccountState() {
        binding.clValidationAccount.visibility = View.VISIBLE
    }

    private fun hideValidationAccountState() {
        binding.clValidationAccount.visibility = View.GONE
    }

    private fun showNoValidatedAccountState() {
        binding.clNoValidatedAccount.visibility = View.VISIBLE
    }
}