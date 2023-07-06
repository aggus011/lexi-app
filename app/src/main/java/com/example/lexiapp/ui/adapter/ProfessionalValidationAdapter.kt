package com.example.lexiapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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

        @SuppressLint("ClickableViewAccessibility")
        fun setSwitchListener(professional: ProfessionalValidation) {
            binding.switchValidate.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    showConfirmationDialog(professional)
                }
                true
            }
        }

        private fun showConfirmationDialog(professional: ProfessionalValidation) {
            val message = if (binding.switchValidate.isChecked) "¿Desea habilitar al profesional: " else "¿Desea deshabilitar al profesional: "

            val builder = AlertDialog.Builder(binding.root.context)
            builder.setMessage("$message ${professional.name}, matrícula ${professional.medicalRegistration}?")
                .setPositiveButton("Confirmar") { _, _ ->
                    binding.switchValidate.isChecked = !binding.switchValidate.isChecked // Cambiar el estado del interruptor
                    setApproval(professional.email, binding.switchValidate.isChecked) // Llama a la acción de confirmación
                }
                .setNegativeButton("Cancelar", null)
            builder.create().show()
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