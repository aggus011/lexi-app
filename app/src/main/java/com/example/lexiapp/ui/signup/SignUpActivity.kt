package com.example.lexiapp.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.lexiapp.databinding.ActivitySignUpBinding
import com.example.lexiapp.domain.useCases.LoginUseCases
import com.example.lexiapp.ui.login.LoginViewModel
import com.example.lexiapp.utils.FirebaseResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        val validationObserver = Observer<FirebaseResult> { authResult ->
            if (authResult is FirebaseResult.TaskSuccess) {
                Toast.makeText(this, "Se registró correctamente", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Hubo un error", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.registerResult.observe(this, validationObserver)
        setUp()
    }

    private fun setUp() {
        binding.btnSignUp.setOnClickListener {
            if(fieldsNotEmpty() && fieldsNotNull()){
                viewModel.singUpWithEmail(
                    binding.etEmail.editText?.text.toString(),
                    binding.etPassword.editText?.text.toString()
                )
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