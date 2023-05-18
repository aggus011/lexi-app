package com.example.lexiapp.ui.games.whereistheletter

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
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.lexiapp.R
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationClient
import com.example.lexiapp.data.api.word_asociation_api.WordAssociationService
import com.example.lexiapp.databinding.ActivityWhereIsTheLetterBinding
import com.example.lexiapp.data.api.LetterRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@AndroidEntryPoint
class WhereIsTheLetterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWhereIsTheLetterBinding

    //Should be inject
    private lateinit var vM: WhereIsTheLetterViewModel

    private var positions = mutableMapOf<Int, Button>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhereIsTheLetterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setVM()
        progressBarOn()
        setListeners()
        waitForValues()
    }

    private fun waitForValues() {
        lifecycleScope.launch {
            delay(2500)
            withContext(Dispatchers.Main) {
                setValues()
            }
            progressBarOff()
        }
    }

    private fun progressBarOn(){
        lifecycleScope.launch {
            binding.progressBar.visibility= View.VISIBLE
            binding.txtWord.visibility= View.GONE
            binding.txtVariableWord.visibility= View.GONE
            binding.iconVolume.visibility= View.GONE
            binding.txtFindLetter.visibility= View.GONE
        }
    }

    private fun progressBarOff(){
        binding.progressBar.visibility= View.GONE
        binding.txtWord.visibility= View.VISIBLE
        binding.txtVariableWord.visibility= View.VISIBLE
        binding.iconVolume.visibility= View.VISIBLE
        binding.txtFindLetter.visibility= View.VISIBLE
    }



    private fun listenerOtherWord() {
        binding.btnOtherWord.setOnClickListener {
            vM.onPositionDeselected()
            progressBarOn()
            deleteBtnLetter()
            vM.onOmitWord()
            waitForValues()
        }
    }

    private fun setListeners() {
        listenerResult()
        listenerOtherWord()
    }

    private fun listenerResult() {
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
            //Go to ActivityGoodResult
            Toast.makeText(this, "Felicidades, elegiste la letra ${vM.basicWord.value[vM.selectedPosition.value!!]}", Toast.LENGTH_SHORT).show()
            vM.resetSubmit()
        } else {
            //Go to ActivityBadResult
            Toast.makeText(this, "No es la letra correcta, elegiste la letra ${vM.basicWord.value[vM.selectedPosition.value!!]}", Toast.LENGTH_SHORT).show()
            vM.resetSubmit()
        }
    }

    private fun deleteBtnLetter() {
        if(positions.isNotEmpty()){
            positions.forEach {
                binding.containerWord.removeView(it.value)
            }
        }
        positions.clear()
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
        if(vM.isItSelected(pos)){
            vM.onPositionDeselected()
            return
        }
        vM.onPositionSelected(pos)
        setStyle(pos, SetStyleButton.SELECTED)
    }

    private fun setValues() {
        val word = vM.basicWord.value
        Log.v("init_word_in_activity", "response word: $word")
        binding.txtVariableWord.text = word
        binding.txtVariableLetter.text = word[vM.correctPosition.value].toString()
        for (letter in word.withIndex()){
            createWordButton(letter.value.uppercase(), { onLetterSelected(letter.index) }) { btn ->
                saveBtnPosition(
                    pos = letter.index,
                    btn = btn
                )
            }
        }
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

    //Should be inject
    private fun setVM() {
        val service= Retrofit.Builder()
            .baseUrl("https://api.wordassociations.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WordAssociationService::class.java)
        val client= WordAssociationClient(service)
        val repo= LetterRepository(client)
        val factory = WhereIsTheLetterViewModel.Factory(repo) // Factory
        vM = ViewModelProvider(this, factory)[WhereIsTheLetterViewModel::class.java]
    }
}
enum class SetStyleButton{
    SELECTED,
    NOT_SELECTED
}