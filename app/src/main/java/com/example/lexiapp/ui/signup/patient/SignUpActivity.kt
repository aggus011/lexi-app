package com.example.lexiapp.ui.signup.patient

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.ui.customDialog.DialogFragmentLauncher
import com.example.lexiapp.ui.customDialog.ErrorDialog
import com.example.lexiapp.databinding.ActivitySignUpBinding
import com.example.lexiapp.ui.login.LoginActivity
import com.example.lexiapp.domain.model.UserSignUp
import com.example.lexiapp.ui.customDialog.show
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    @Inject
    lateinit var dialogLauncher: DialogFragmentLauncher
    companion object {
        fun create(context: Context): Intent =
            Intent(context, SignUpActivity::class.java)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.navigateToLogin.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToLogin()
            }
        }
        viewModel.showErrorDialog.observe(this) { showError ->
            if (showError) showErrorDialog()
        }
        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun showErrorDialog() {
        ErrorDialog.create(
            title = "Ha ocurrido un error",
            description = "No hemos podido crear tu cuenta, intenta denuevo mas tarde",
            positiveAction = ErrorDialog.Action("Ok") {
                it.dismiss()
            }
        ).show(dialogLauncher, this)
    }

    private fun goToLogin() {
        startActivity(LoginActivity.create(this))
    }

    private fun initListeners() {
        binding.btnSignUp.setOnClickListener {
            if (fieldsNotEmpty() && fieldsNotNull()) {
                viewModel.singUpWithEmail(
                    UserSignUp(
                    binding.etName.editText?.text.toString(),
                    binding.etEmail.editText?.text.toString(),
                    binding.etConfirmEmail.editText?.text.toString(),
                    binding.etPassword.editText?.text.toString(),
                    binding.etConfirmPassword.editText?.text.toString()
                    )
                )
            }
        }

    }

    private fun fieldsNotEmpty(): Boolean =
        binding.etEmail.editText?.text!!.isNotEmpty() && binding.etPassword.editText?.text!!.isNotEmpty()

    private fun fieldsNotNull(): Boolean =
        binding.etEmail.editText?.text != null && binding.etPassword.editText?.text != null
}