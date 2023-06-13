package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ItemPatientBinding
import com.example.lexiapp.domain.model.Patient

class PatientAdapter(
    private val patientList: List<Patient>,
    private val onClickPatient: (Patient) -> Unit,
    private val onClickDelete: (String) -> Unit
): RecyclerView.Adapter<PatientAdapter.PatientAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientAdapterViewHolder {
        val patientBinding =
            ItemPatientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientAdapterViewHolder(patientBinding)
    }

    override fun onBindViewHolder(holder: PatientAdapterViewHolder, position: Int) {
        val patient = patientList[position]
        holder.bind(patient)
        holder.setListener (patient)/*{
            val expanded = patient.expanded
            patient.expanded=!expanded
            notifyItemChanged(position)
        }*/
    }

    override fun getItemCount(): Int {
        return patientList.size
    }

    inner class PatientAdapterViewHolder(val binding: ItemPatientBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(patient: Patient) {
            //Set item View
            /*binding.clExpanded.visibility = if (patient.expanded) View.VISIBLE else View.GONE
            binding.txtSeeMore.visibility = if (patient.expanded) View.GONE else View.VISIBLE
            binding.icSeeMore.visibility = if (patient.expanded) View.GONE else View.VISIBLE*/
            //Set data
            binding.txtName.text=patient.user.userName
            binding.txtEmail.text=patient.user.email
            //binding.txtResultExample.text=patient.results
        }
        fun setListener(patient: Patient) {
            /*binding.icSeeMore.setOnClickListener {
                setExpandedView()
            }
            binding.icClose.setOnClickListener {
                setExpandedView()
            }*/
            binding.btnTrash.setOnClickListener{
                onClickDelete(patient.user.email)
            }
            binding.clDetailPatient.setOnClickListener{
                onClickPatient(patient)
            }
        }
    }
}

