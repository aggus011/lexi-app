package com.example.lexiapp.ui.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivitySplashBinding
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.domain.useCases.ProfileUseCase
import com.example.lexiapp.ui.MainActivity
import com.example.lexiapp.ui.login.LoginActivity
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    @Inject
    lateinit var profileUseCase: ProfileUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))
        //To force app to only light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)
        verifyCurrentSession()
    }

    private fun verifyCurrentSession(){
        if(profileUseCase.getEmail()!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}