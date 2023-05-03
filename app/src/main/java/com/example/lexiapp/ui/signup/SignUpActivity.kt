package com.example.lexiapp.ui.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivitySignUpBinding
import com.example.lexiapp.domain.AuthProvider

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    lateinit var authProv: AuthProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        authProv = AuthProvider()
        setUp()
    }

    private fun setUp() {
        binding.btnSignUp.setOnClickListener {
            if(fieldsNotEmpty() && fieldsNotNull()){
                authProv.singUpWithEmail(
                    binding.etEmail.editText?.text.toString(),
                    binding.etPassword.editText?.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Se registró correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Hubo un error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun fieldsNotEmpty(): Boolean=binding.etEmail.editText?.text!!.isNotEmpty() && binding.etPassword.editText?.text!!.isNotEmpty()
    private fun fieldsNotNull(): Boolean=binding.etEmail.editText?.text!=null && binding.etPassword.editText?.text!=null
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