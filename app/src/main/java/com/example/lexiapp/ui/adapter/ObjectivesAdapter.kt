package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ObjectiveItemBinding
import com.example.lexiapp.domain.model.Objective

class ObjectivesAdapter : RecyclerView.Adapter<ObjectivesAdapter.ObjectiveViewHolder>() {
    private val objectiveList = mutableListOf<Objective>()
    private var buttonClickListener: ((Objective) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectiveViewHolder {
        val objectiveBinding =
            ObjectiveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjectiveViewHolder(objectiveBinding)
    }

    override fun onBindViewHolder(holder: ObjectiveViewHolder, position: Int) {
        val objective = objectiveList[position]
        holder.bind(objective)
    }

    override fun getItemCount(): Int {
        return objectiveList.size
    }

    fun updateObjectiveList(objectives: List<Objective>) {
        objectiveList.clear()
        objectiveList.addAll(objectives)
        notifyDataSetChanged()
    }

    inner class ObjectiveViewHolder(private val binding: ObjectiveItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val button: View = binding.btnObjective // Cambio aquí

        fun bind(objective: Objective) {
            val progress = if(objective.progress <= objective.goal!!) objective.progress else objective.goal
            binding.tvObjectiveTitle.text = objective.title
            binding.tvObjectiveProgress.text = "${progress}/${objective.goal}"
        }
    }



}
