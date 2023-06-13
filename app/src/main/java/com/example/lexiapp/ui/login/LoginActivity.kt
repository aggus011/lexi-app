package com.example.lexiapp.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lexiapp.R
import com.example.lexiapp.ui.customDialog.DialogFragmentLauncher
import com.example.lexiapp.ui.customDialog.ErrorDialog
import com.example.lexiapp.databinding.ActivityLoginBinding
import com.example.lexiapp.ui.MainActivity
import com.example.lexiapp.domain.model.UserLogin
import com.example.lexiapp.ui.signup.SignUpActivity
import com.example.lexiapp.ui.customDialog.show
import com.example.lexiapp.ui.login.changepassword.ChangePasswordDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var dialogLauncher: DialogFragmentLauncher

    companion object {
        fun create(context: Context): Intent =
            Intent(context, LoginActivity::class.java)

    }

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val validationObserver = Observer<FirebaseResult> { authResult ->
            when(authResult::class) {
                FirebaseResult.TaskSuccess::class -> {
                    saveSesion(binding.etMail.editText?.text!!.toString())
                    goToHome()
                }
                FirebaseResult.TaskFaliure::class -> showAlert("Error en registro")
                FirebaseResult.EmailError::class -> showAlert("Email Inválido")
                FirebaseResult.PasswordError::class -> showAlert("Contraseña débil")
            }
        }
        viewModel.authResult.observe(this, validationObserver)
        //setUpLoginEmail()
        setUpRegister()
    }
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPrefs()
        initUI()
    }

    private fun initPrefs() {
       prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
    }

    private fun initUI() {
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.btnLogin.setOnClickListener {
            if (binding.etMail.editText?.text!!.isNotEmpty() && binding.etPassword.editText?.text!!.isNotEmpty()) {
                viewModel.loginUser(
                    binding.etMail.editText?.text!!.toString().trim(),
                    binding.etPassword.editText?.text!!.toString()
                )
            }
        }
        binding.tvForgetPassword.setOnClickListener {
            ChangePasswordDialogFragment()
                .show(supportFragmentManager, "change_password_dialog")
        }
        setUpRegister()
    }

    private fun initObservers() {
        viewModel.navigateToHome.observe(this) {
            it.getContentIfNotHandled()?.let {
                goToHome()
            }
        }
        viewModel.showErrorDialog.observe(this) { userLogin ->
            if (userLogin.showErrorDialog) showErrorDialog(userLogin)
        }

        /*
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { viewState ->
                updateUI(viewState)
            }
        }
                 */
    }

    private fun showErrorDialog(userLogin: UserLogin) {
        ErrorDialog.create(
            title = "No hemos encontrado el usuario",
            description = "Verifique los datos o vuelva a intentarlo",
            negativeAction = ErrorDialog.Action("Cancelar") {
                it.dismiss()
            },
            positiveAction = ErrorDialog.Action("Reintentar") {
                viewModel.loginUser(
                    userLogin.email,
                    userLogin.password
                )
                it.dismiss()
            }
        ).show(dialogLauncher, this)
    }

    private fun setUpRegister() {
        binding.tvRegister.setOnClickListener {
            startActivity(SignUpActivity.create(this))
        }
    }

    private fun goToHome() {
        //Intent a la homePage
        Toast.makeText(this, "Se accedió correctamente", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }
}
