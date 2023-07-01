package com.example.lexiapp.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ActivityAdminBinding
import com.example.lexiapp.ui.adapter.ProfessionalValidationAdapter
import com.example.lexiapp.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    private val vM: AdminViewModel by viewModels()
    private lateinit var rvProfessionalValidation: RecyclerView
    private lateinit var professionalValidationAdapter: ProfessionalValidationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.btnLogout.setOnClickListener {
            closeSession()
        }
    }

    private fun closeSession() {
        vM.closeSession()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onPause() {
        super.onPause()

        closeSession()
    }
}