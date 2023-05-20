package com.example.lexiapp.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityLoginBinding
import com.example.lexiapp.ui.MainActivity
import com.example.lexiapp.ui.signup.SignUpActivity
import com.example.lexiapp.utils.FirebaseResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    //private lateinit var authProv: LoginUseCases
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val validationObserver = Observer<FirebaseResult> { authResult ->
            if (authResult is FirebaseResult.TaskSuccess) {
                saveSesion(binding.etMail.editText?.text!!.toString())
                goToHome()
            } else {
                showAlert()
            }
        }
        viewModel.authResult.observe(this, validationObserver)
        setUpLoginEmail()
        setUpRegister()
    }

    private fun setUpRegister() {
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun validateSesion() {
        if ((prefs.getString("email", null)) != null) {
            //showHomeActivity(prefs.getString("email",null))
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun saveSesion(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    private fun setUpLoginEmail() {
        binding.btnLogin.setOnClickListener {
            if (binding.etMail.editText?.text!!.isNotEmpty() && binding.etPassword.editText?.text!!.isNotEmpty()) {
                viewModel.loginEmail(
                    binding.etMail.editText?.text!!.toString(),
                    binding.etPassword.editText?.text!!.toString()
                )
            }
        }
    }


    private fun goToHome() {
        //Intent a la homePage
        Toast.makeText(this, "Se accedió correctamente", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun showAlert() {
        val alert = AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Se ha producido un erron en la autenticación")
            .setPositiveButton("Aceptar", null)
            .create()
        alert.show()
    }
}
