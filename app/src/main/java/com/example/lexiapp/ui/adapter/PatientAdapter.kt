package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ItemPatientBinding
import com.example.lexiapp.databinding.ObjectiveItemBinding
import com.example.lexiapp.domain.model.Objective
import com.example.lexiapp.domain.model.Patient
import com.example.lexiapp.domain.model.TextToRead

class PatientAdapter(
    private val patientList: List<Patient>,
    private val onClick: (Patient) -> Unit
): RecyclerView.Adapter<PatientAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientAdapterViewHolder {
        val patientBinding =
            ItemPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientAdapterViewHolder(patientBinding)
    }

    override fun onBindViewHolder(holder: PatientAdapterViewHolder, position: Int) {
        val patient = patientList[position]
        //Set data with holder.binding

    }

    override fun getItemCount(): Int {
        return patientList.size
    }

}

class PatientAdapterViewHolder(val binding: ItemPatientBinding): RecyclerView.ViewHolder(binding.root)