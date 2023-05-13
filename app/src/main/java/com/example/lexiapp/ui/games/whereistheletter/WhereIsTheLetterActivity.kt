package com.example.lexiapp.ui.games.whereistheletter

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityWhereIsTheLetterBinding

class WhereIsTheLetterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWhereIsTheLetterBinding

    //Should be inject
    private lateinit var vM: WhereIsTheLetterViewModel

    private val positions = mutableMapOf<Int, Button>()

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
            vM.onSubmitAnswer()
            if(vM.isAnyLetterSelected()){
                validateLetterSelected()
            }else{
                Toast.makeText(this, "Debe elegir una letra", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateLetterSelected() {
        if(vM.correctAnswerSubmitted.value) {
            //Go to ActivityGoodReult
            Toast.makeText(this, "Felicidades, elegiste la letra ${vM.basicWord.value[vM.selectedPosition.value!!]}", Toast.LENGTH_SHORT).show()
            vM.resetSubmit()
        } else {
            //Go to ActivityGoodReult
            Toast.makeText(this, "No es la letra correcta, elegiste la letra ${vM.basicWord.value[vM.selectedPosition.value!!]}", Toast.LENGTH_SHORT).show()
            vM.resetSubmit()
        }
    }

    private fun setValues() {
        val word = vM.basicWord.value
        binding.txtVariableWord.text = word
        binding.txtVariableLetter.text = word[vM.correctPosition.value].toString()
        for (letter in word.withIndex()) {
            createWordButton(letter.value.uppercase(), { onLetterSelected(letter.index) }) { btn ->
                saveBtnPosition(
                    pos = letter.index,
                    btn = btn
                )
            }
        }
    }

    private fun onLetterSelected(pos: Int) {
        resetAllBtns()
        if(vM.isItSelected(pos)){
            vM.onPositionDeselected()
            return
        }
        vM.onPositionSelected(pos)
        setStyle(pos, SetStyleButton.SELECTED)
    }

    private fun resetAllBtns() {
        for (pos in positions.keys) {
            setStyle(pos, SetStyleButton.NOT_SELECTED)
        }
    }

    private fun setStyle(pos: Int, type: SetStyleButton){
        when(type){
            SetStyleButton.NOT_SELECTED->{
                positions[pos]!!.background = ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_not_select)
                positions[pos]!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))
            }
            SetStyleButton.SELECTED->{
                positions[pos]!!.background = ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_select)
                positions[pos]!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            }
        }
    }

    private fun saveBtnPosition(btn: Button, pos: Int) {
        positions[pos] = btn
    }

    private fun createWordButton(
        letter: String,
        listener: () -> Unit,
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
        params.gravity=Gravity.CENTER
        btnLetter.layoutParams = params
        btnLetter.typeface = Typeface.DEFAULT_BOLD
        btnLetter.background =
            ContextCompat.getDrawable(applicationContext, R.drawable.btn_letter_not_select)
        btnLetter.setOnClickListener {
            listener()
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
enum class SetStyleButton{
    SELECTED,
    NOT_SELECTED
}