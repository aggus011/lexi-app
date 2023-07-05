package com.example.lexiapp.ui.games.letsread.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityNegativeResultCorrectWordBinding
import com.example.lexiapp.databinding.ActivityPositiveResultLetsReadBinding
import com.example.lexiapp.ui.games.correctword.CorrectWordViewModel
import com.example.lexiapp.ui.games.letsread.ListTextActivity
import com.example.lexiapp.ui.games.letsread.TextViewModel
import com.example.lexiapp.ui.patienthome.HomePatientActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PositiveResultLetsReadActivity : AppCompatActivity() {
    lateinit var binding: ActivityPositiveResultLetsReadBinding
    private val vM: TextViewModel by viewModels()

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
        }
        homeBtn.setOnClickListener {
            startActivity(Intent(this, HomePatientActivity::class.java))
        }
    }
}