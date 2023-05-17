package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ObjectiveItemBinding
import com.example.lexiapp.domain.model.Objective

class ObjectivesAdapter: RecyclerView.Adapter<ObjectivesAdapterViewHolder>() {
    private val objectiveList = mutableListOf<Objective>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectivesAdapterViewHolder {
        val objectiveBinding =
            ObjectiveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ObjectivesAdapterViewHolder(objectiveBinding)
    }

    override fun onBindViewHolder(holder: ObjectivesAdapterViewHolder, position: Int) {
        val objective = objectiveList[position]

        holder.binding.tvObjectiveTitle.text = objective.title
        holder.binding.tvObjectiveProgress.text = objective.progress.toString().plus("/").plus(objective.goal.toString())
    }

    override fun getItemCount(): Int {
        return objectiveList.size
    }

    fun updateObjectiveList(objectives: List<Objective>){
        if(objectives != null){
            objectiveList.clear()
            objectiveList.addAll(objectives)
        }
    }
}

class ObjectivesAdapterViewHolder(val binding: ObjectiveItemBinding): RecyclerView.ViewHolder(binding.root)