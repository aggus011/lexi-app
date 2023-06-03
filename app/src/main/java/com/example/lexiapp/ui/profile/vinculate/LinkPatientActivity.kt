package com.example.lexiapp.ui.profile.vinculate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityLinkPatientBinding
import com.example.lexiapp.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinkPatientActivity : AppCompatActivity() {
    lateinit var binding: ActivityLinkPatientBinding
    private val vM: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLinkPatientBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setListeners()
        setQr()
    }

    private fun setListeners() {

    }

    private fun setQr(){
        binding.ivQr.setImageBitmap(vM.getQR())
    }

}