package com.example.textrecognitionxml

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

private lateinit var photoTaken: File

class MainActivity : AppCompatActivity() {
    companion object {
        private const val IMAGE_CHOOSE = 1000
        private const val TAKE_PHOTO = 1001
        private const val PERMISSION_CODE = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnPickImage: Button = findViewById(R.id.btnPickImage)
        val btnTakePhoto: Button = findViewById(R.id.btnTakePhoto)

        btnTakePhoto.setOnClickListener {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoTaken = getPhotoFile()
            val providerFile = FileProvider.getUriForFile(
                this,
                "com.example.textrecognitionxml.fileprovider",
                photoTaken
            )
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)

            try {
                startActivityForResult(takePhotoIntent, TAKE_PHOTO)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Camera could not open", Toast.LENGTH_SHORT).show()
            }
        }

        btnPickImage.setOnClickListener {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                chooseImageGallery()
            }
        }
    }

    private fun chooseImageGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_CHOOSE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
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

    private fun getPhotoFile(): File {
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("photo", ".jpg", directoryStorage)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val ivImage: ImageView = findViewById(R.id.ivImage)
        val tvExtractedText: TextView = findViewById(R.id.tvExtractedText)

        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            val takenPhoto = BitmapFactory.decodeFile(photoTaken.absolutePath)
            extractedText(tvExtractedText, ivImage, takenPhoto)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val chosenPhotoBitmap =
                MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
            extractedText(tvExtractedText, ivImage, chosenPhotoBitmap)
        }
    }

    private fun extractedText(textView: TextView, imageView: ImageView, bitmap: Bitmap) {
        val recognizer: TextRecognizer =
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val inputImage = InputImage.fromBitmap(bitmap, 0)

        imageView.setImageBitmap(bitmap)

        inputImage.let { image ->
            recognizer.process(image).addOnSuccessListener {
                textView.text = it.text
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to extract text", Toast.LENGTH_SHORT).show()
            }
        }
    }
}