package com.example.lexiapp.ui.textscanner

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lexiapp.databinding.ActivityTextScannerBinding
import com.google.android.material.button.MaterialButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TextScannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTextScannerBinding
    private lateinit var btnBack: ImageButton
    private lateinit var photoToScan: ImageView
    private lateinit var textRecognized: TextView
    private lateinit var btnReScan: MaterialButton
    private lateinit var btnReadText: MaterialButton

    private var imageUri: Uri? = null

    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    private lateinit var textRecognizer: TextRecognizer
    private lateinit var textToSpeech: TextToSpeech

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                hideBlackScreen()
                photoToScan.setImageURI(imageUri)
                recognizeTextFromImage()
            }else{
                //When i close de camera, finish the activity
                finish()
            }
        }

    private val galleryActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                hideBlackScreen()
                val data = result.data
                imageUri = data!!.data
                photoToScan.setImageURI(imageUri)
                recognizeTextFromImage()
            }else{
                //No one image picked from gallery
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTextScannerBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //To handle when user do back gesture
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        initArraysPermissions()
        checkImageInput(intent.extras)
        getViews()
        btnBackListener()
        setTextRecognizer()
        setTextToSpeech()
        setListeners()
    }

    private fun initArraysPermissions(){
        cameraPermissions = arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        storagePermissions = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun checkImageInput(extras: Bundle?) {
        if(extras != null){
            if(extras.getInt("InputImage") == 1){
                verifyCameraPermissions()
            }else{
                verifyStoragePermissions()
            }
        }
    }

    private fun getViews() {
        photoToScan = binding.ivPhoto
        textRecognized = binding.tvScannedText
        btnReScan = binding.btnReScan
        btnReadText = binding.btnReadText
        btnBack = binding.btnArrowBack
    }

    private fun btnBackListener(){
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setTextRecognizer() {
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    }

    private fun setTextToSpeech(){
        val language = Locale("es", "US")
        textToSpeech = TextToSpeech(this) {
            if (it != TextToSpeech.ERROR) {
                textToSpeech.language = language
            }
        }
    }

    private fun setListeners() {
        setBtnReTakePhotoListener()
        setBtnReadTextRecognizedListener()
    }

    private fun setBtnReTakePhotoListener() {
        btnReScan.setOnClickListener {
            verifyCameraPermissions()
            cleanPreviousRecognizedText()
        }
    }

    private fun cleanPreviousRecognizedText() {
        textRecognized.text = ""
    }

    private fun setBtnReadTextRecognizedListener() {
        btnReadText.setOnClickListener {
            if(!textToSpeech.isSpeaking){
                readRecognizedText()
            }else{
                stopCurrentReading()
            }
        }
    }

    private fun readRecognizedText() {
        if(textRecognized.text.isNotEmpty()) {
            textToSpeech
                .speak(textRecognized.text.toString(), TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    private fun stopCurrentReading() {
        textToSpeech.stop()
    }

    private fun takePhoto(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Lexi Photo")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        cameraActivityResultLauncher.launch(intent)
    }

    private fun pickImageFromGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        galleryActivityResultLauncher.launch(intent)
    }

    private fun recognizeTextFromImage(){
        if(imageUri != null){
            try {
                val inputImage = InputImage.fromFilePath(this, imageUri!!)

                val textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener { text ->
                        val recognizedText = text.text
                        if(recognizedText.isNotEmpty()){
                            textRecognized.text = recognizedText
                            textRecognized.movementMethod = ScrollingMovementMethod()
                            binding.clNoTextImage.visibility = View.GONE
                        }else{
                            binding.clNoTextImage.visibility = View.VISIBLE
                        }
                    }
                    .addOnFailureListener{
                        //A failure had occurred
                    }
            }catch (e: Exception){
                //Handle exception
            }
        }
    }

    private fun verifyCameraPermissions(){
        if(checkCameraPermissions()){
            takePhoto()
        }else{
            requestCameraPermissions()
            showBlackScreen()
        }
    }

    private fun verifyStoragePermissions(){
        if(checkStoragePermissions()){
            pickImageFromGallery()
        }else{
            requestStoragePermissions()
            showBlackScreen()
        }
    }

    private fun checkCameraPermissions(): Boolean{
        val cameraResult = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val storageResult = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

        return cameraResult && storageResult
    }

    private fun checkStoragePermissions(): Boolean{
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermissions(){
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE)
    }

    private fun requestStoragePermissions(){
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE)
    }

    private fun showBlackScreen() {
        binding.vBlackScreen.visibility = View.VISIBLE
    }

    private fun hideBlackScreen() {
        binding.vBlackScreen.visibility = View.GONE
    }

    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private companion object {
        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 200
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //handle permission(s) results
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (cameraAccepted && storageAccepted) {
                        takePhoto()
                    } else {
                        Toast.makeText(this, "Necesitas darnos permiso para acceder a la cámara", Toast.LENGTH_SHORT).show()
                        //No camera and storage permissions granted
                        finish()
                    }

                }
            }
            STORAGE_REQUEST_CODE -> {
                val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED

                if(storageAccepted){
                    //Open gallery
                    pickImageFromGallery()
                }else{
                    Toast.makeText(this, "Necesitas darnos permiso para acceder a la galeria", Toast.LENGTH_SHORT).show()
                    //No storage permissions granted
                    finish()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        textToSpeech.stop()
    }
    override fun onDestroy() {
        super.onDestroy()
        cleanPreviousRecognizedText()
        textToSpeech.shutdown()
    }
}