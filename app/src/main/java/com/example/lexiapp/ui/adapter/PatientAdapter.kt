package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ItemPatientBinding
import com.example.lexiapp.domain.model.Patient

class PatientAdapter(
    private val patientList: List<Patient>,
    private val onClick: (Patient) -> Unit
): RecyclerView.Adapter<PatientAdapter.PatientAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientAdapterViewHolder {
        val patientBinding =
            ItemPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientAdapterViewHolder(patientBinding)
    }

    override fun onBindViewHolder(holder: PatientAdapterViewHolder, position: Int) {
        holder.bind(patientList[position])
    }

    override fun getItemCount(): Int {
        return patientList.size
    }

    inner class PatientAdapterViewHolder(val binding: ItemPatientBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(patient: Patient) {
            //Set data
            binding.txtName.text=patient.user?.userName
            binding.txtEmail.text=patient.user?.email
        }
    }
}

