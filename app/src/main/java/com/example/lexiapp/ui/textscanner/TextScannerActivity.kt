package com.example.lexiapp.ui.textscanner

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.lexiapp.R
import com.example.lexiapp.databinding.ActivityTextScannerBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TextScannerActivity : AppCompatActivity() {
    private val vM: TextScannerViewModel by viewModels()

    private lateinit var binding: ActivityTextScannerBinding
    private lateinit var btnBack: ImageView
    private lateinit var photoToScan: ImageView
    private lateinit var textRecognized: TextView
    private lateinit var btnReScan: MaterialButton
    private lateinit var btnReadText: MaterialButton
    private lateinit var progressBar: ProgressBar

    private var imageUri: Uri? = null

    private lateinit var cameraPermissions: Array<String>
    private lateinit var storagePermissions: Array<String>

    private lateinit var textRecognizer: TextRecognizer
    private lateinit var textToSpeech: TextToSpeech

    private val cropImage =
        registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            photoToScan.setImageURI(result.uriContent)
            recognizeTextFromImage(result.uriContent)
        } else {
            // An error occurred
            Log.v(TAG, "${result.error!!.message}")
            binding.clNoTextImage.visibility = View.VISIBLE
        }
    }


    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                hideBlackScreen()
                cropImage()
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
                cropImage()
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
        btnHelpListener()
        btnBackListener()
        setTextRecognizer()
        setTextToSpeech()
        setListeners()
    }

    private fun initArraysPermissions(){
        cameraPermissions = arrayOf(android.Manifest.permission.CAMERA,
            if(verifyApiVersionIsHigherThat32()){
                android.Manifest.permission.READ_MEDIA_IMAGES
            }else{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            })

        storagePermissions = if(verifyApiVersionIsHigherThat32()){
            arrayOf(android.Manifest.permission.READ_MEDIA_IMAGES)
        }else{
            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
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
        btnBack = binding.btnBack
        progressBar = binding.pbLoadingRecognizedText
    }

    private fun btnHelpListener() {
        binding.btnHelp.setOnClickListener {
            setAlertBuilderToGoToYoutube()
        }
    }

    private fun setAlertBuilderToGoToYoutube() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Tutorial de Palabra Correcta")
            .setMessage("¿Desea salir de LEXI e ir a YouTube?")
            .setPositiveButton("SI"){dialog, _ ->
                try{
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(TUTORIAL_LINK)
                    intent.setPackage("com.google.android.youtube")
                    startActivity(intent)
                }catch (e:Exception){
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(TUTORIAL_LINK)))
                }

            }
            .setNegativeButton("NO") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

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
                textToSpeech.setSpeechRate(0.6f)
            }
        }
    }

    private fun setListeners() {
        setInputImageDialog()
        setBtnReadTextRecognizedListener()
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

    private fun recognizeTextFromImage(imageUri: Uri?) {
        if(imageUri != null){
            progressBar.visibility = View.VISIBLE
            try {
                val inputImage = InputImage.fromFilePath(this, imageUri)

                textRecognizer.process(inputImage)
                    .addOnSuccessListener { text ->
                        val recognizedText = text.text
                        if(recognizedText.isNotEmpty()){
                            textRecognized.text = recognizedText
                            textRecognized.movementMethod = ScrollingMovementMethod()
                            binding.clNoTextImage.visibility = View.GONE
                            vM.saveTSResult()
                            vM.checkIfObjectiveHasBeenCompleted("SCAN", "play", "Escanear foto")
                        }else{
                            binding.clNoTextImage.visibility = View.VISIBLE
                        }
                        progressBar.visibility = View.GONE
                    }
                    .addOnFailureListener{
                        //A failure had occurred
                        binding.clNoTextImage.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Log.v(TAG, "failure text recognition ${it.message}")
                    }
            }catch (e: Exception){
                //Handle exception
                binding.clNoTextImage.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
                Log.v(TAG, "exception ${e.message}")
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

    private fun cropImage(){
        cropImage.launch(
            CropImageContractOptions(
                uri = imageUri,
                cropImageOptions = CropImageOptions(
                    imageSourceIncludeGallery = true,
                    imageSourceIncludeCamera = true,
                    allowFlipping = false,
                    toolbarColor = ContextCompat.getColor(this, R.color.blue),
                    progressBarColor = ContextCompat.getColor(this, R.color.blue),
                    activityTitle = getString(R.string.choose_what_scan)
                )
            )
        )
    }

    private fun verifyApiVersionIsHigherThat32(): Boolean{
        return android.os.Build.VERSION.SDK_INT >
                android.os.Build.VERSION_CODES.S_V2
    }

    private fun setInputImageDialog() {
        val popUpMenu = PopupMenu(this, btnReScan)

        popUpMenu.inflate(R.menu.input_image)

        setBtnScanListener(popUpMenu)

        popUpMenu.setOnMenuItemClickListener { menuItem ->
            goToScannerActivity(menuItem.itemId)
            true
        }
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun setBtnScanListener(popUpMenu: PopupMenu) {
        btnReScan.setOnClickListener {
            try {
                val popup = PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popUpMenu)
                menu.javaClass
                    .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu, true)

            } catch (e: Exception) {
                Log.e("MainError", e.toString())
            } finally {
                popUpMenu.show()
            }
        }
    }

    private fun goToScannerActivity(itemId: Int) {
        val intent = Intent(this, TextScannerActivity::class.java)

        when (itemId) {
            R.id.camera -> intent.putExtra("InputImage", 1)
            R.id.gallery -> intent.putExtra("InputImage", 2)
        }

        startActivity(intent)
        finish()
    }

    private val onBackPressedCallback: OnBackPressedCallback = object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
        }
    }

    private companion object {
        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 200
        private const val TAG = "TextScannerActivity"
        private const val TUTORIAL_LINK = "https://www.youtube.com/shorts/qxuh3YwJNac"
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