package com.example.lexiapp.ui.games.letsread.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.lexiapp.databinding.ActivityPositiveResultLetsReadBinding
import com.example.lexiapp.ui.games.letsread.ListTextActivity
import com.example.lexiapp.ui.patienthome.HomePatientActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PositiveResultLetsReadActivity : AppCompatActivity() {
    lateinit var binding: ActivityPositiveResultLetsReadBinding

    private lateinit var nextTextBtn: Button
    private lateinit var homeBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositiveResultLetsReadBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        getViews()
        setButton()
    }

    private fun getViews() {
        nextTextBtn = binding.btnNextText
        homeBtn = binding.btnGoInit
    }

    private fun setButton() {
        nextTextBtn.setOnClickListener {
            startActivity(Intent(this, ListTextActivity::class.java))
            finish()
        }
        homeBtn.setOnClickListener {
            startActivity(Intent(this, HomePatientActivity::class.java))
            finish()
        }
    }
}