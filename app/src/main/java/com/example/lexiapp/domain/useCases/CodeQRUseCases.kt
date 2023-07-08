package com.example.lexiapp.domain.useCases

import android.graphics.Bitmap
import android.text.TextUtils
import com.example.lexiapp.ui.profesionalhome.CaptureAct
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.journeyapps.barcodescanner.ScanOptions
import javax.inject.Inject

class CodeQRUseCases @Inject constructor(){
    private fun isVerified(contents: String) = (CODE_VERIFY == TextUtils.substring(
        contents,
        0,
        CODE_VERIFY.length
    ))

    fun getScanOptions(): ScanOptions {
        val options = ScanOptions()
        options
            .setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            .setPrompt("Escaneá el QR del paciente a vincular")
            .setBeepEnabled(true)
            .setOrientationLocked(true)
            .captureActivity = CaptureAct::class.java
        return options
    }

    private fun deserialize(emailJson: String): String {
        return Gson().fromJson(
            TextUtils.substring(emailJson, CODE_VERIFY.length, emailJson.length),
            EMAIL_STRING
        )
    }

    fun getEmailFromQR(contents: String) = if (isVerified(contents)) deserialize(contents) else null


    fun generateQR(email: String): Bitmap? {
        return BarcodeEncoder().encodeBitmap(
            CODE_VERIFY+email, BarcodeFormat.QR_CODE,
            1150, 1150)
    }

    companion object {
        private val EMAIL_STRING = object : TypeToken<String>() {}.type
        private val CODE_VERIFY = "LEXIAPP2023".hashCode().toString()
    }
}