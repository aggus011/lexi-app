package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ItemProfessionalVerificationBinding
import com.example.lexiapp.domain.model.ProfessionalValidation

class ProfessionalValidationAdapter(
    private val professionals: List<ProfessionalValidation>
): RecyclerView.Adapter<ProfessionalValidationAdapter.ProfessionalValidationViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfessionalValidationAdapter.ProfessionalValidationViewHolder {
        return ProfessionalValidationViewHolder(
            ItemProfessionalVerificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ProfessionalValidationAdapter.ProfessionalValidationViewHolder,
        position: Int
    ) {
        val professional = professionals[position]

        holder.binding.tvProfessionalName.text = professional.name
        holder.binding.tvProfessionalEmail.text = professional.email
        holder.binding.tvMedicalRegistration.text = professional.medicalRegistration
        holder.binding.switchValidate.isChecked = professional.validated
    }

    override fun getItemCount(): Int {
        return professionals.size
    }

    inner class ProfessionalValidationViewHolder(val binding: ItemProfessionalVerificationBinding): RecyclerView.ViewHolder(binding.root)
}