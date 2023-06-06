package com.example.lexiapp.ui.profile.edit

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import androidx.activity.viewModels
import com.example.lexiapp.databinding.ActivityEditProfileBinding
import com.example.lexiapp.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private lateinit var binding: ActivityEditProfileBinding
    private val vM: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        setDatePicker()
        setListeners()
    }

    private fun setDatePicker() {
        //binding.datePicker.init(0,0,0,null)
        val calendarMin = Calendar.getInstance()
        calendarMin.add(Calendar.YEAR, -120)
        val minDate = calendarMin.timeInMillis

        val calendarMax = Calendar.getInstance()
        calendarMax.add(Calendar.YEAR, -1)
        val maxDate=calendarMax.timeInMillis

        binding.datePicker.minDate = minDate
        binding.datePicker.maxDate = maxDate
    }

    private fun setListeners() {
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnConfirm.setOnClickListener {
            //getSelectedDate()
            vM.modifiedProfile(getName(), null)
            finish()
        }

        binding.datePicker.setOnDateChangedListener { datePicker, i, i2, i3 ->
            getSelectedDate()
        }
    }

    private fun getName(): String?{
        var name: String?=binding.etName.editText?.text.toString()
        if (name != null) {
            if (name.isEmpty() || name.isBlank()) name=null
        }
        return name
    }
    private fun getSelectedDate(/*year: Int, month: Int, day: Int*/) {
        //validation withActualDate
        val datePicker = binding.datePicker
        val year = datePicker.year
        val month = datePicker.month
        val day = datePicker.dayOfMonth
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        vM.setBirthDate(calendar)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        //getSelectedDate(year,month, dayOfMonth)
    }
}