package com.example.lexiapp.ui.signup

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.core.dialog.DialogFragmentLauncher
import com.example.lexiapp.core.dialog.ErrorDialog
import com.example.lexiapp.databinding.ActivitySignUpBinding
import com.example.lexiapp.ui.login.LoginActivity
import com.example.lexiapp.core.ex.*
import com.example.lexiapp.domain.model.UserSignUp
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
        /*
        val validationObserver = Observer<FirebaseResult> { authResult ->
            when (authResult::class) {
                FirebaseResult.TaskSuccess::class -> {
                    Toast.makeText(this, "Se registró correctamente", Toast.LENGTH_SHORT).show()
                }
                FirebaseResult.TaskFaliure::class -> Toast.makeText(
                    this,
                    "Hubo un error",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {}
            }
        }
        viewModel.registerResult.observe(this, validationObserver)
        setUp()

         */
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
//RegisterActivity
/*lateinit var authProv: AuthProvider
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authProv= AuthProvider()
        binding.h.setOnClickListener{ startActivity(Intent(this,LoginActivity::class.java)) }
        setUp()
    }
    private fun setUp(){
        binding.register.setOnClickListener{
            if (binding.emailRegister.text!!.isNotEmpty() && binding.passRegister.text!!.isNotEmpty()){
                //For create a New User use .createUserWhitEmailAndPassword(e,p)
                authProv.singUpWithEmail(binding.emailRegister.text.toString(),binding.passRegister.text.toString()).addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Se registró correctamente",Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Hubo un error",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }*/