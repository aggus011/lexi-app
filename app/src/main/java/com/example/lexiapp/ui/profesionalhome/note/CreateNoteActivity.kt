package com.example.lexiapp.ui.profesionalhome.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityCreateNoteBinding
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNoteBinding
    private val vM: NoteViewModel by viewModels()
    private lateinit var noteCharsCounter: TextView

    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setPatientData(Gson().fromJson(intent.getStringExtra("patient"), User::class.java))
        setNoteCharsCounter()
        setListener()
        setObserver()
    }

    private fun setNoteCharsCounter() {
        noteCharsCounter = binding.tvNoteCharsCounter

        binding.txtNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val remainingCharacters = NOTE_MAX_LENGTH - s?.length!!
                val color = when {
                    remainingCharacters >= 100 -> R.color.green
                    remainingCharacters >= 50 -> R.color.pear_green
                    else -> R.color.red
                }

                noteCharsCounter.text = remainingCharacters.toString().plus("/200")
                noteCharsCounter.setTextColor(ContextCompat.getColor(this@CreateNoteActivity, color))
            }
        })
    }

    private fun setPatientData(patient: User) {
        binding.tvUserInitials.text = patient.userName?.firstOrNull()?.uppercase() ?: "L"
        binding.txtName.text = patient.userName
        binding.txtEmail.text = patient.email
        email = patient.email
    }

    private fun setObserver() {
        vM.resultSave.observe(this) { result->
            when(result){
                FirebaseResult.TaskSuccess->{
                    toast("Se guardó la nota")
                    binding.txtNote.text.clear()
                    finish()
                }
                FirebaseResult.TaskFailure->{
                    toast("No se pudo guardar")
                }
                else -> {}
            }
        }
    }

    private fun setListener() {
        binding.btnCreateNote.setOnClickListener{
            vM.saveNote(email, getTextNote())
        }

        binding.icClose.setOnClickListener {
            finish()
        }
    }

    private fun getTextNote()=binding.txtNote.text.toString()

    private fun toast(txtToast: String) {
        Toast.makeText(this, txtToast, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val NOTE_MAX_LENGTH = 200
    }

}