package com.example.lexiapp.ui.profesionalhome.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityRecordNoteBinding
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Note
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.ui.adapter.NoteAdapter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecordNoteBinding
    private val vM: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordNoteBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setObserver()
        setPatient()
        setRV()
    }

    private fun setObserver() {
        vM.patient.observe(this) { patient ->
            setProfile(patient!!)
            vM.startListenerOfNotes()
        }
        vM.resultDelete.observe(this) { result->
            when(result){
                FirebaseResult.TaskSuccess->toast("Se eliminó la nota")
                FirebaseResult.TaskFaliure->toast("No se pudo eliminar")
                else -> {}
            }
        }
    }

    private fun setProfile(patient: User) {
        //binding.tvUserInitials.text= (patient.userName?.get(0) ?: "") as CharSequence?
        binding.txtName.text = patient.userName
        binding.txtEmail.text = patient.email
    }

    private fun toast(txtToast: String) {
        Toast.makeText(this, txtToast, Toast.LENGTH_SHORT).show()
    }

    private fun setRV() {
        binding.rvNote.layoutManager=LinearLayoutManager(this)
        vM.notes.observe(this){ notes->
            binding.rvNote.adapter=NoteAdapter(notes, ::deleteNote)
        }
    }

    private fun setPatient() {
        vM.setPatient(Gson().fromJson(intent.getStringExtra("patient"), User::class.java))
    }

    private fun cleanPatient() {
        vM.cleanPatient()
    }

    private fun deleteNote(note: Note) = vM.deleteNote(note)


    override fun onDestroy() {
        super.onDestroy()
        cleanPatient()
    }
}