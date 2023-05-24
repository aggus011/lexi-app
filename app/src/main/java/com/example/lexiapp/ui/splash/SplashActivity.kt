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
import com.example.lexiapp.ui.MainActivity
import com.example.lexiapp.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(LayoutInflater.from(this))

        //To force app to only light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(binding.root)

        setPreferences()
        verifyCurrentSession()

    }

    private fun setPreferences() {
        prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
    }

    private fun verifyCurrentSession(){
        if((prefs.getString("email",null))!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}