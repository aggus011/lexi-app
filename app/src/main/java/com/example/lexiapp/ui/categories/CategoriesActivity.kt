package com.example.lexiapp.ui.categories

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityCategoriesBinding
import com.example.lexiapp.ui.patienthome.HomePatientActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class CategoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var btnReadPrincipalTitle: ImageView
    private lateinit var btnGoHome: MaterialButton
    private var categoriesCount: Int = 0
    private var categoriesNames = mutableListOf<String>()

    private lateinit var textToSpeech: TextToSpeech

    private val viewModel: CategoriesViewModel by viewModels()

    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoriesBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //To handle when user do back gesture
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        getViews()
        checkSelectionCount()
        setTextToSpeech()
        setObservers()
        setListeners()
    }

    private fun getViews() {
        btnReadPrincipalTitle = binding.ivIcRead
        btnGoHome = binding.mbContinueToHome
    }

    private fun setTextToSpeech(){
        val language = Locale("es", "US")

        textToSpeech = TextToSpeech(this) {
            if (it != TextToSpeech.ERROR) {
                textToSpeech.language = language
                textToSpeech.setSpeechRate(0.6f)
            }
        }
    }

    private fun setListeners() {
        setTitleSpeechToTextListener()
        setCheckBoxesListeners()
        setButtonContinueToHome()
    }

    private fun setTitleSpeechToTextListener(){
        btnReadPrincipalTitle.setOnClickListener {
            speechText(binding.tvCategories.text.toString())
        }
    }

    private fun setCheckBoxesListeners(){
        setSelectionListener(binding.checkboxCategory1)
        setSelectionListener(binding.checkboxCategory2)
        setSelectionListener(binding.checkboxCategory3)
        setSelectionListener(binding.checkboxCategory4)
        setSelectionListener(binding.checkboxCategory5)
        setSelectionListener(binding.checkboxCategory6)
        setSelectionListener(binding.checkboxCategory7)
        setSelectionListener(binding.checkboxCategory8)
        setSelectionListener(binding.checkboxCategory9)
    }

    private fun setSelectionListener(button: MaterialCheckBox) {
        button.isSelected = false

        button.setOnClickListener {
            if (button.isSelected) {
                categoriesCount--
                button.isSelected = false
                categoriesNames.remove(button.text.toString())
            } else {
                categoriesCount++
                button.isSelected = true
                categoriesNames.add(button.text.toString())
            }
            checkSelectionCount()
        }

        button.setOnLongClickListener {
            speechText(button.text.toString())
            true
        }
    }

    private fun checkSelectionCount() {
        if(categoriesCount == 3){
            enableBtnGoHome()
        }else{
            disableBtnGoHome()
        }

        if(categoriesCount > 3){
            Toast
                .makeText(this, "Por favor elige solo 3", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun enableBtnGoHome() {
        btnGoHome.isEnabled = true
        btnGoHome.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.light_purple, theme))
        btnGoHome.setTextColor(resources.getColor(R.color.white, theme))
    }

    private fun disableBtnGoHome() {
        btnGoHome.isEnabled = false
        btnGoHome.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.profile_background, theme))
        btnGoHome.setTextColor(resources.getColor(R.color.white, theme))
    }

    private fun speechText(textToSpeech: String){
        if(this.textToSpeech.isSpeaking){
            this.textToSpeech.stop()
        }

        this.textToSpeech
            .speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun setButtonContinueToHome() {
       btnGoHome.setOnClickListener {
           if(categoriesCount == 3 && categoriesNames.isNotEmpty()){
               viewModel.saveCategoriesFromPatient(categoriesNames)
           }
       }
    }

    private fun setObservers(){
        viewModel.categoriesSavedSuccessful.observe(this){
            if(it){
                startActivity(Intent(this, HomePatientActivity::class.java))
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.shutdown()
    }
}