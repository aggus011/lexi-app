package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ItemNoteBinding
import com.example.lexiapp.domain.model.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(
    private val listNote: List<Note>,
    private val deleteNote: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NoteViewHolder {
        val noteBinding =
            ItemNoteBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return NoteViewHolder(noteBinding)
    }

    override fun onBindViewHolder(viewHolder: NoteViewHolder, position: Int) {
        val item = listNote[position]
        viewHolder.bind(item)
        viewHolder.setListeners(item)
    }

    override fun getItemCount() = listNote.size

    inner class NoteViewHolder(val binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            binding.txtNote.text=note.text
            binding.txtValueDate.text=getDateFormatted(note.date!!)
        }

        fun setListeners(note: Note) {
            binding.btnTrash.setOnClickListener {
                deleteNote(note)
            }
        }

        private fun getDateFormatted(dateMillis: String): String {
            val date = Date(dateMillis.toLong())
            val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return format.format(date)
        }
    }
}