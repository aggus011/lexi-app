package com.example.lexiapp.ui.games.whereistheletter

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityWhereIsTheLetterBinding

class WhereIsTheLetterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWhereIsTheLetterBinding

    //Should be inject
    private lateinit var vM: WhereIsTheLetterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWhereIsTheLetterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setVM()
        setListenerResult()
        setValues()
    }

    private fun setListenerResult() {
        binding.btnResult.setOnClickListener {
            //Validation: isSomeLetterSelected()-> if(is true)-> Go to result
            //chamge UI of button
        }
    }

    private fun setValues() {
        val word = vM.basicWord.value
        /*
        if (word == null) {
            Toast.makeText(this, "No se pudo encontrar una palabra", Toast.LENGTH_SHORT).show()
            //Handle response null from api
            return
        }

         */
        binding.txtVariableWord.text = word?.uppercase() ?: ""
        binding.txtVariableLetter.text = word?.get(vM.correctPosition.value)?.toString()?.uppercase() ?: "?"
        for (letter in (word?.toCharArray()) ?: "asdfasd".toCharArray()) {
            createWordButton(letter.toString().uppercase())
        }
    }

    private fun createWordButton(letter: String) {
        val btnLetter = Button(this)
        btnLetter.text = letter
        btnLetter.textSize = 38F
        val params = LinearLayout.LayoutParams(
            100, 120
        )
        params.setMargins(8, 8, 8, 8)
        btnLetter.layoutParams = params
        btnLetter.typeface = Typeface.DEFAULT_BOLD
        btnLetter.background =
            ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_not_select)
        btnLetter.setOnClickListener {
            btnLetter.background =
                ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_select)
            btnLetter.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
        }
        binding.containerWord.addView(btnLetter)
    }

    //Should be inject
    private fun setVM() {
        //Should be inject
        val factory = WhereIsTheLetterViewModel.Factory() // Factory
        vM = ViewModelProvider(this, factory)[WhereIsTheLetterViewModel::class.java]
    }
}