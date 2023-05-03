package com.example.lexiapp.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.example.lexiapp.R
import com.example.lexiapp.domain.AuthProvider
import com.example.lexiapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.signin.SignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

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
    }

    private fun validateSesion() {
        if((prefs.getString("email",null))!=null){
            //showHomeActivity(prefs.getString("email",null))
            startActivity(Intent(this,HomeActivity::class.java))
        }
    }

    private fun saveSeion(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    private fun setUpLoginEmail(){
        binding.btnLogin.setOnClickListener{
            if (binding.etMail.text!!.isNotEmpty() && binding.etPassword.text!!.isNotEmpty()){
                authProv.loginEmail(binding.etMail.text.toString(),binding.etPassword.text.toString()).addOnCompleteListener {
                    if (it.isSuccessful) {
                        saveSeion(binding.etMail.text.toString())
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
        startActivity(Intent(this,HomeActivity::class.java))
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