package com.example.lexiapp.ui.games.whereistheletter.result

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityNegativeResultWhereIsTheLetterBinding
import com.example.lexiapp.ui.games.whereistheletter.WhereIsTheLetterActivity
import com.example.lexiapp.ui.games.whereistheletter.WhereIsTheLetterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class NegativeResultWhereIsTheLetterActivity : AppCompatActivity() {
    lateinit var binding: ActivityNegativeResultWhereIsTheLetterBinding
    private lateinit var word: String
    private var answerPosition by Delegates.notNull<Int>()
    private var correctPosition by Delegates.notNull<Int>()
    private val vM: WhereIsTheLetterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNegativeResultWhereIsTheLetterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        checkObjectives()
        getValues()
        setWords(word)
        setListeners()
    }

    private fun checkObjectives() {
        vM.checkIfObjectivesHasBeenCompleted("WL", "play", "¿Dónde está la letra?")
    }

    private fun getValues() {
        word = intent.getStringExtra("word").toString()
        answerPosition = intent.getIntExtra("answerPosition", 0)
        correctPosition = intent.getIntExtra("correctPosition", 0)
        Log.v("VALUES_ACT_WITLNR", "word:$word/" +
                "answer:$answerPosition/correct:$correctPosition")
    }

    private fun setWords(word: String?) {
        Log.v("WORD_LETTER_ACT_NEGA", "$word")
        if(word!=null){
            for (letter in word.withIndex()){
                createView(letter.value.uppercase(), letter.index, TypeWord.ANSWER)
                createView(letter.value.uppercase(), letter.index, TypeWord.CORRECT)
            }
        }
    }

    private fun createView(
        letter: String,
        position: Int,
        type: TypeWord
    ) {
        val view = getView(type, position)
        val viewLetter = setViewLetter(view, letter)
        addToLayout(type, viewLetter)
    }

    private fun addToLayout(type: TypeWord, viewLetter: TextView) {
        Log.v("WORD_LETTER_ACT_NEGA", "$correctPosition")
        Log.v("WORD_LETTER_ACT_NEGA", "$answerPosition")
        when(type){
            TypeWord.ANSWER-> binding.lyAnswer.addView(viewLetter)
            TypeWord.CORRECT-> binding.lyResultCorrect.addView(viewLetter)
        }
    }

    private fun getView(type: TypeWord, position: Int): TextView = when(type){
            TypeWord.ANSWER-> getViewWithSytle(position, answerPosition)
            TypeWord.CORRECT-> getViewWithSytle(position, correctPosition)
        }

    private fun getViewWithSytle(position: Int, positionTwo: Int): TextView{
        val viewLetter = TextView(this)
        if (position==positionTwo) {
            viewLetter.background = ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_select)
            viewLetter.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
        }else{
            viewLetter.background = ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_not_select)
            viewLetter.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
        }
        return viewLetter
    }


    private fun setViewLetter(view: TextView, letter: String): TextView {
        view.text = letter
        view.textSize = 22.8F
        val params = LinearLayout.LayoutParams(
            60, 72
        )
        params.setMargins(4, 4, 4, 4)
        params.gravity = Gravity.CENTER
        view.layoutParams = params
        view.typeface = Typeface.DEFAULT_BOLD
        return view
    }

    private fun setListeners() {
        goToNextWord()
        goToHome()
    }

    private fun goToNextWord() {
        binding.btnNextWord.setOnClickListener {
            startActivity(Intent(this, WhereIsTheLetterActivity::class.java))
            finish()
        }
    }

    private fun goToHome() {
        binding.btnGoInit.setOnClickListener {
            finish()
        }
    }
}
enum class TypeWord{
    ANSWER,
    CORRECT
}