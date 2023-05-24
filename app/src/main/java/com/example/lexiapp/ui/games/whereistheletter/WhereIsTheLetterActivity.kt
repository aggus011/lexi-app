package com.example.lexiapp.ui.games.whereistheletter

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityWhereIsTheLetterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.wait

@AndroidEntryPoint
class WhereIsTheLetterActivity : AppCompatActivity() {

    private val vM: WhereIsTheLetterViewModel by viewModels()

    private lateinit var binding: ActivityWhereIsTheLetterBinding

    //Should be inject

    private var positions = mutableMapOf<Int, Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhereIsTheLetterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        vM.onOmitWord()
        setWordObserver()
        progressBarOn()
        setListeners()
    }

    private fun setWordObserver() {
        vM.basicWord.observe(this) {
            if (it.isNullOrEmpty()) {
                progressBarOn()
            } else {
                binding.btnOtherWord.isClickable = false
                setValues()
                progressBarOff()
            }
            binding.btnOtherWord.isClickable = true
        }
        vM.letter.observe(this) {
            binding.txtVariableLetter.text = it.uppercase()
        }
    }

    private fun progressBarOn() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE
                binding.txtWord.visibility = View.GONE
                binding.txtVariableWord.visibility = View.GONE
                binding.iconVolume.visibility = View.GONE
                binding.txtFindLetter.visibility = View.GONE
            }
        }
    }

    private fun progressBarOff() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                binding.txtWord.visibility = View.VISIBLE
                binding.txtVariableWord.visibility = View.VISIBLE
                binding.iconVolume.visibility = View.VISIBLE
                binding.txtFindLetter.visibility = View.VISIBLE
            }
        }
    }

    private fun callWordFail() {
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                binding.txtWord.visibility = View.GONE
                binding.txtVariableWord.visibility = View.GONE
                binding.iconVolume.visibility = View.GONE
                binding.txtFindLetter.visibility = View.GONE
            }
        }
        Toast.makeText(this, "FALLO, ELEGIR OTRA PALABRA", Toast.LENGTH_SHORT).show()
    }


    private fun listenerOtherWord() {
        binding.btnOtherWord.setOnClickListener {
            vM.onPositionDeselected()
            progressBarOn()
            deleteBtnLetter()
            vM.onOmitWord()
        }
    }

    private fun setListeners() {
        listenerResult()
        listenerOtherWord()
    }

    private fun listenerResult() {
        binding.btnResult.setOnClickListener {
            if (vM.isAnyLetterSelected()) {
                vM.onSubmitAnswer()
                validateLetterSelected()
            } else {
                Toast.makeText(this, "Debe elegir una letra", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLetterSelected() {
        if (vM.correctAnswerSubmitted.value == true) {
            //Go to ActivityGoodResult
            Log.v("CORRECT_SUBMIT", "WORD: ${vM.basicWord.value} ")
            Log.v("CORRECT_SUBMIT", "POSITION: ${vM.selectedPosition.value} ")
            Toast.makeText(
                this,
                "Felicidades, elegiste la letra ${vM.getLetterWithPosition()}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            //Go to ActivityBadResult
            Log.v("INCORRECT_SUBMIT", "WORD: ${vM.basicWord.value} ")
            Log.v("INCORRECT_SUBMIT", "POSITION: ${vM.selectedPosition.value} ")
            Toast.makeText(
                this,
                "No es la letra correcta, elegiste la letra ${vM.getLetterWithPosition()}",
                Toast.LENGTH_SHORT
            ).show()
        }
        vM.resetSubmit()
        //NO DEBERIAN IR, SE DEBERIAN LIMPIAR LAS VARIABLE
        vM.onOmitWord()
        deleteBtnLetter()
    }

    private fun deleteBtnLetter() {
        if (positions.isNotEmpty()) {
            positions.forEach {
                binding.containerWord.removeView(it.value)
            }
        }
        positions.clear()
        vM.cleanBasicWord()
    }

    private fun resetAllBtns() {
        positions.keys.forEach {
            setStyle(it, SetStyleButton.NOT_SELECTED)
        }
    }

    private fun saveBtnPosition(btn: Button, pos: Int) {
        positions[pos] = btn
    }

    private fun onLetterSelected(pos: Int) {
        resetAllBtns()
        if (vM.selectedPosition.value == pos) {
            vM.onPositionDeselected()
        } else {
            vM.onPositionSelected(pos)
            setStyle(pos, SetStyleButton.SELECTED)
        }

    }

    private fun setValues() {
        val word = vM.basicWord.value!!
        Log.v("init_word_in_activity", "response word: $word")
        binding.txtVariableWord.text = word
        //binding.txtVariableLetter.text = word[vM.correctPosition.value!!].toString()
        for (letter in word.withIndex()) {
            createWordButton(letter.value.uppercase(), letter.index, ::onLetterSelected) { btn ->
                saveBtnPosition(
                    pos = letter.index,
                    btn = btn
                )
            }
        }
    }

    private fun createWordButton(
        letter: String,
        index: Int,
        listener: (Int) -> Unit,
        saveBtn: (Button) -> Unit
    ) {
        val btnLetter = Button(this)
        saveBtn(btnLetter)
        btnLetter.text = letter
        btnLetter.textSize = 38F
        val params = LinearLayout.LayoutParams(
            100, 120
        )
        params.setMargins(8, 8, 8, 8)
        params.gravity = Gravity.CENTER
        btnLetter.layoutParams = params
        btnLetter.typeface = Typeface.DEFAULT_BOLD
        btnLetter.background =
            ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_not_select)
        btnLetter.setOnClickListener {
            listener.invoke(index)
        }
        binding.containerWord.addView(btnLetter)
    }

    private fun setStyle(pos: Int, type: SetStyleButton) {
        when (type) {
            SetStyleButton.NOT_SELECTED -> {
                positions[pos]!!.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_not_select)
                positions[pos]!!.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.black
                    )
                )
            }

            SetStyleButton.SELECTED -> {
                positions[pos]!!.background =
                    ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_select)
                positions[pos]!!.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.white
                    )
                )
            }
        }
    }
}

enum class SetStyleButton {
    SELECTED,
    NOT_SELECTED
}