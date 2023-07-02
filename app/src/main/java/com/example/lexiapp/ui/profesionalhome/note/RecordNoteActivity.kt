package com.example.lexiapp.ui.profesionalhome.note

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lexiapp.databinding.ActivityRecordNoteBinding
import com.example.lexiapp.domain.model.FirebaseResult
import com.example.lexiapp.domain.model.Note
import com.example.lexiapp.domain.model.User
import com.example.lexiapp.ui.adapter.NoteAdapter
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

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
                FirebaseResult.TaskFailure->toast("No se pudo eliminar")
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


    private fun deleteNote(note: Note) {
        showConfirmationDialog(note)
    }

    private fun showConfirmationDialog(note: Note) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Esta seguro de que quiere borrar la nota de fecha: ${getDateFormatted(note.date!!)}?")
            .setPositiveButton("Confirmar") { _, _ ->
                // Llama a la acción de confirmación
                delete(note)
            }
            .setNegativeButton("Cancelar", null)
        builder.create().show()
    }

    private fun delete(note: Note) = vM.deleteNote(note)

    private fun getDateFormatted(dateMillis: String): String {
        val date = Date(dateMillis.toLong())
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }

    override fun onDestroy() {
        super.onDestroy()
        cleanPatient()
    }
}