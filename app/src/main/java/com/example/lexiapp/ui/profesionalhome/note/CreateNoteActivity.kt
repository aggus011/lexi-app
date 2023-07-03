package com.example.lexiapp.ui.profesionalhome.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityCreateNoteBinding
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.User
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNoteBinding
    private val vM: NoteViewModel by viewModels()

    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setPatientData(Gson().fromJson(intent.getStringExtra("patient"), User::class.java))
        setListener()
        setObserver()
    }

    private fun setPatientData(patient: User) {
        binding.tvUserInitials.text = patient.userName?.firstOrNull()?.uppercase() ?: "L"
        binding.txtName.text = patient.userName
        binding.txtEmail.text = patient.email
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

}