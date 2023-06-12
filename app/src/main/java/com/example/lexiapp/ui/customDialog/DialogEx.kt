package com.example.lexiapp.ui.customDialog

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

fun DialogFragment.show(launcher: DialogFragmentLauncher, activity: FragmentActivity) {
    launcher.show(this, activity)
}