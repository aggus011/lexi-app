package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ItemTitleTextBinding
import com.example.lexiapp.domain.model.TextToRead

class TextAdapter (
        private val listText: List<TextToRead>,
        private val onClick: (TextToRead) -> Unit
    ) : RecyclerView.Adapter<TextAdapter.TextViewHolder>() {

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TextViewHolder {
            val textBinding = ItemTitleTextBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            return TextViewHolder(textBinding)
        }

        override fun onBindViewHolder(viewHolder: TextViewHolder, position: Int) {
            val item = listText[position]
            viewHolder.bind(item)
            viewHolder.itemView.setOnClickListener {}
            viewHolder.binding.btnTitleText.setOnClickListener {
                onClick(item)
            }
        }

        override fun getItemCount() = listText.size

        class TextViewHolder(val binding : ItemTitleTextBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(textToRead: TextToRead) = with(binding.btnTitleText) {
                text=textToRead.title
            }
        }
    }
