package com.example.lexiapp.ui.signup.professional

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lexiapp.databinding.ActivityProfessionalSignUpBinding
import com.example.lexiapp.domain.model.ProfessionalSignUp
import com.example.lexiapp.ui.customDialog.DialogFragmentLauncher
import com.example.lexiapp.ui.customDialog.ErrorDialog
import com.example.lexiapp.ui.customDialog.show
import com.example.lexiapp.ui.login.LoginActivity
import com.example.lexiapp.ui.verifymedicalregistration.VerifyMedicalRegistrationActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfessionalSignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfessionalSignUpBinding
    private lateinit var userName: TextInputLayout
    private lateinit var userSurname: TextInputLayout
    private lateinit var medicalRegistration: TextInputLayout
    private lateinit var email: TextInputLayout
    private lateinit var confirmEmail: TextInputLayout
    private lateinit var password: TextInputLayout
    private lateinit var confirmPassword: TextInputLayout
    private lateinit var btnSignUp: MaterialButton
    private val viewModel: ProfessionalSignUpViewModel by viewModels()

    @Inject
    lateinit var dialogLauncher: DialogFragmentLauncher

    companion object {
        fun create(context: Context): Intent =
            Intent(context, ProfessionalSignUpActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfessionalSignUpBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        checkIfProfessionalIsBeingVerified()
        getViews()
        initObservers()
        setListeners()
    }

    private fun checkIfProfessionalIsBeingVerified() {
        viewModel.isProfessionalAccountBeenVerified()
    }

    private fun getViews() {
        userName = binding.etName
        userSurname = binding.etSurname
        medicalRegistration = binding.etMedicalRegistration
        email = binding.etEmail
        confirmEmail = binding.etConfirmEmail
        password = binding.etPassword
        confirmPassword = binding.etConfirmPassword
        btnSignUp = binding.btnSignUp
    }

    private fun initObservers() {
        viewModel.showErrorDialog.observe(this) { showError ->
            if (showError) showErrorDialog(viewModel.errorMessage)
        }
        viewModel.signUpSuccess.observe(this) {
            if (it) {
                startActivity(Intent(this, VerifyMedicalRegistrationActivity::class.java))
                finish()
            }
        }
        viewModel.isProfessionalAccountBeenVerified.observe(this) {
            if (it) {
                startActivity(Intent(this, VerifyMedicalRegistrationActivity::class.java))
                finish()
            }
        }
    }

    private fun showErrorDialog(message: String = "No hemos podido crear tu cuenta, intenta denuevo mas tarde") {
        ErrorDialog.create(
            title = "Ha ocurrido un error",
            description = message,
            positiveAction = ErrorDialog.Action("OK") {
                it.dismiss()
            }
        ).show(dialogLauncher, this)
    }

    private fun setListeners() {
        btnSignUp.setOnClickListener {
            if (areTextInputLayoutsNotEmpty(
                    userName,
                    userSurname,
                    medicalRegistration,
                    email,
                    confirmEmail,
                    password,
                    confirmPassword
                )
            ) {
                viewModel.signUpWithEmail(
                    ProfessionalSignUp(
                        userName.editText?.text.toString().trim(),
                        userSurname.editText?.text.toString().trim(),
                        medicalRegistration.editText?.text.toString().trim(),
                        email.editText?.text.toString().trim(),
                        confirmEmail.editText?.text.toString().trim(),
                        password.editText?.text.toString().trim(),
                        confirmPassword.editText?.text.toString().trim()
                    )
                )
            } else {
                Toast.makeText(this, "Algún campo vacío", Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun areTextInputLayoutsNotEmpty(vararg textInputLayouts: TextInputLayout): Boolean {
        for (textInputLayout in textInputLayouts) {
            val editText = textInputLayout.editText
            val text = editText?.text?.toString()?.trim()
            if (text.isNullOrEmpty()) {
                return false
            }
        }
        return true
    }
}