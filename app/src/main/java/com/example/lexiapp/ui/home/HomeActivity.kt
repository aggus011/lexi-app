package com.example.lexiapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityHomeBinding
import com.example.lexiapp.domain.AuthProvider

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var authProv: AuthProvider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authProv= AuthProvider()
        prefs=getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        setLogOut()
    }

    private fun setLogOut() {
        binding.btnLogout.setOnClickListener{
            prefs.edit().clear().apply()
            authProv.singOut()
            onBackPressed()
        }
    }
}