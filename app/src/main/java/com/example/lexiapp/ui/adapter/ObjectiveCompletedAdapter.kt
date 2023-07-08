package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ItemCompleteObjectiveBinding
import com.example.lexiapp.domain.model.MiniObjective

class ObjectiveCompletedAdapter(
    val objectives: List<MiniObjective>
): RecyclerView.Adapter<ObjectiveCompletedAdapter.ObjectiveViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ObjectiveViewHolder {
        return ObjectiveViewHolder(
            ItemCompleteObjectiveBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ObjectiveViewHolder, position: Int) {
        val item = objectives[position]
        viewHolder.bind(item)
    }

    override fun getItemCount() = objectives.size

    inner class ObjectiveViewHolder(val binding: ItemCompleteObjectiveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(objective: MiniObjective) {
            binding.txtDateComplete.text = "Fecha: ${objective.date}"
            binding.tvObjectiveTitle.text = objective.title
            binding.tvObjectiveProgress.text = "${objective.count}/${objective.count}"
        }
    }

}

