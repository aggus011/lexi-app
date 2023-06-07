package com.example.lexiapp.ui.profile.vinculate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityLinkPatientBinding
import com.example.lexiapp.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinkPatientActivity : AppCompatActivity() {
    lateinit var binding: ActivityLinkPatientBinding
    private val vM: ProfileViewModel by viewModels()

    private lateinit var countdownTimer: CountDownTimer
    private var timeLeftInMillis: Long = 60000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLinkPatientBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setQr()
    }

    private fun setQr(){
        binding.ivQr.setImageBitmap(vM.getQR())
    }

    private fun startTimer() {
        countdownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val seconds = millisUntilFinished / 1000
                binding.txtTimerCount.text = seconds.toString()
            }

            override fun onFinish() {
                finish()
            }
        }
        countdownTimer.start()
    }

    override fun onResume() {
        super.onResume()
        startTimer()
    }

    override fun onPause() {
        super.onPause()
        countdownTimer.cancel()
    }
}