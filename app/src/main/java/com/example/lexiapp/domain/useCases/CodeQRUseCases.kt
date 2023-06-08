package com.example.lexiapp.domain.useCases

import android.graphics.Bitmap
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import javax.inject.Inject

class CodeQRUseCases @Inject constructor(){
    fun isVerified(contents: String?) = (CODE_VERIFY == TextUtils.substring(
        contents,
        0,
        CODE_VERIFY.length
    ))

    fun deserializeEmail(emailJson: String): String {
        return Gson().fromJson(
            TextUtils.substring(emailJson, CODE_VERIFY.length, emailJson.length),
            EMAIL_STRING
        )
    }

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