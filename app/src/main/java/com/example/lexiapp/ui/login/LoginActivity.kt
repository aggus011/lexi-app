package com.example.lexiapp.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isNotEmpty
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityLoginBinding
import com.example.lexiapp.domain.AuthProvider
import com.example.lexiapp.ui.home.HomeActivity
import com.example.lexiapp.ui.signup.SignUpActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignInClient

class LoginActivity : AppCompatActivity() {
    private lateinit var prefs: SharedPreferences
    private lateinit var authProv: AuthProvider
    private lateinit var binding : ActivityLoginBinding
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authProv= AuthProvider()
        prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        validateSesion()
        setUpLoginEmail()
        setUpRegister()
    }

    private fun setUpRegister() {
        binding.tvRegister.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }

    private fun validateSesion() {
        if((prefs.getString("email",null))!=null){
            //showHomeActivity(prefs.getString("email",null))
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun saveSeion(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    private fun setUpLoginEmail(){
        binding.btnLogin.setOnClickListener{
            if (binding.etMail.editText?.text!!.isNotEmpty() && binding.etPassword.editText?.text!!.isNotEmpty()){
                authProv.loginEmail(binding.etMail.editText?.text!!.toString(),binding.etPassword.editText?.text!!.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveSeion(binding.etMail.editText?.text!!.toString())
                        goToHome()
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }


    private fun goToHome() {
        //Intent a la homePage
        Toast.makeText(this, "Se accedió correctamente",Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun showAlert(){
        val alert = AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Se ha producido un erron en la autenticación")
            .setPositiveButton("Aceptar", null)
            .create()
        alert.show()
    }
}
