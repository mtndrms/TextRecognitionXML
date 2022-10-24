package com.example.textrecognitionxml

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPickImage: Button = findViewById(R.id.btnPickImage)

        /*
        btnTakePhoto.setOnClickListener {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            filePhoto = getPhotoFile(FILE_NAME)


            val providerFile =FileProvider.getUriForFile(this,"com.example.androidcamera.fileprovider", filePhoto)
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
            if (takePhotoIntent.resolveActivity(this.packageManager) != null){
                startActivityForResult(takePhotoIntent, REQUEST_CODE)
            }else {
                Toast.makeText(this,"Camera could not open", Toast.LENGTH_SHORT).show()
            }
        }
         */

        btnPickImage.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                chooseImageGallery();
            }
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    companion object {
        private val IMAGE_CHOOSE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseImageGallery()
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val ivImage: ImageView = findViewById(R.id.ivImage)
        val tvExtractedText: TextView = findViewById(R.id.tvExtractedText)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val takenPhoto = BitmapFactory.decodeFile(filePhoto.absolutePath)
            ivImage.setImageBitmap(takenPhoto)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            ivImage.setImageURI(data?.data)
            extractedText(applicationContext, tvExtractedText, data?.data)
        }
    }

    private fun extractedText(applicationContext: Context, textView: TextView, uri: Uri?) {
        val inputImage = InputImage.fromFilePath(applicationContext, uri!!)
        val recognizer: TextRecognizer =
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(inputImage).addOnSuccessListener {
            textView.text = it.text
        }.addOnFailureListener {
            Log.e("ERROR", "Error while extracting text from image.")
        }
    }
}

private const val REQUEST_CODE = 13
private lateinit var filePhoto: File
private const val FILE_NAME = "photo.jpg"