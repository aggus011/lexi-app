package com.example.lexiapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lexiapp.databinding.ItemProfessionalVerificationBinding
import com.example.lexiapp.domain.model.ProfessionalValidation

class ProfessionalValidationAdapter(
    private val professionals: List<ProfessionalValidation>,
    private val setApproval: (String, Boolean) -> Unit
) : RecyclerView.Adapter<ProfessionalValidationAdapter.ProfessionalValidationViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfessionalValidationAdapter.ProfessionalValidationViewHolder {
        return ProfessionalValidationViewHolder(
            ItemProfessionalVerificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ProfessionalValidationAdapter.ProfessionalValidationViewHolder,
        position: Int
    ) {
        val professional = professionals[position]
        holder.bind(professional)
        holder.setSwitchListener(professional)
    }

    override fun getItemCount() = professionals.size

    inner class ProfessionalValidationViewHolder(val binding: ItemProfessionalVerificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(professional: ProfessionalValidation) {
            binding.txtName.text = professional.name
            binding.tvUserInitials.text = userInitials(professional.name)
            binding.txtEmail.text = professional.email
            binding.txtValueMat.text = professional.medicalRegistration
            binding.switchValidate.isChecked = professional.validated
        }

        fun setSwitchListener(professional: ProfessionalValidation) {
            binding.switchValidate.setOnCheckedChangeListener { _, isChecked ->
                setApproval(professional.email, isChecked)
            }
        }

        private fun userInitials(name: String): String {
            val words = name.split(" ")
            val initials = StringBuilder()
            for (word in words) {
                val initial = word.firstOrNull()
                if (initial != null)
                    initials.append(initial)
            }
            return initials.toString()
        }
    }
}