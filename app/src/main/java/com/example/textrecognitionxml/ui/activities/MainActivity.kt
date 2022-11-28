package com.example.textrecognitionxml.ui.activities

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.utils.ScanActivityUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
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

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.navigation)
        val fabGroup: MotionLayout = findViewById(R.id.mlBottomNavigationContainer)
        val fabPickImage: FloatingActionButton = findViewById(R.id.fabPickImage)
        val fabTakePhoto: FloatingActionButton = findViewById(R.id.fabTakePhoto)
        val fabMain: FloatingActionButton = findViewById(R.id.floatingButton)

        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> println("Home")
                R.id.profile -> println("Profile")
                else -> println("Nothing")
            }
            true
        }

        // FIXME: Icon doesn't change
        fabMain.setOnClickListener {
            if (fabGroup.currentState == fabGroup.endState) {
                fabMain.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_scan_outlined
                    )
                )
            } else {
                fabMain.setImageDrawable(
                    ContextCompat.getDrawable(
                        this, R.drawable.ic_scan_filled
                    )
                )
            }
        }

        fabTakePhoto.setOnClickListener {
            fabGroup.transitionToStart()

            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoTaken = getPhotoFile()
            val providerFile = FileProvider.getUriForFile(
                this, "com.example.textrecognitionxml.fileprovider", photoTaken
            )
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)

            try {
                startActivityForResult(takePhotoIntent, TAKE_PHOTO)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Camera could not open", Toast.LENGTH_SHORT).show()
            }
        }

        fabPickImage.setOnClickListener {
            fabGroup.transitionToStart()

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
        val intent = Intent(this, ScanActivity::class.java)
        if (requestCode == TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            val takenPhoto = BitmapFactory.decodeFile(photoTaken.absolutePath)
            ScanActivityUtils.bitmap = takenPhoto
            startActivity(intent)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK) {
            val chosenPhoto = MediaStore.Images.Media.getBitmap(this.contentResolver, data?.data)
            ScanActivityUtils.bitmap = chosenPhoto
            startActivity(intent)
        }
    }
}