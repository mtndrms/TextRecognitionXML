package com.example.textrecognitionxml.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.utils.ScanActivityUtils
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.*

class ScanActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val ivImage: ImageView = findViewById(R.id.ivImage)

        ivImage.setImageBitmap(ScanActivityUtils.bitmap)
        launch {
            extractedText(ScanActivityUtils.bitmap)
        }
    }

    private suspend fun extractedText(bitmap: Bitmap) {
        val recognizer: TextRecognizer =
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val ivLine: ImageView = findViewById(R.id.ivLine)
        val scanAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.scan_animation)

        ScanActivityUtils.bitmap = bitmap

        scanAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                ivLine.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation?) {
                ivLine.visibility = View.INVISIBLE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })

        ivLine.startAnimation(scanAnimation)

        inputImage.let { image ->
            val value = withContext(Dispatchers.Default) {
                async {
                    recognizer.process(image).addOnSuccessListener {
                        ScanActivityUtils.extractedText = it.text
                        detectLanguage()
                    }.addOnFailureListener {
                        Toast.makeText(
                            applicationContext, "Failed to extract text", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            value.await().addOnSuccessListener {
                val timer = object : CountDownTimer(it.text.length.toLong(), 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        ivLine.clearAnimation()
                        val intent = Intent(applicationContext, ScanResultActivity::class.java)
                        startActivity(intent)
                    }
                }
                timer.start()
            }
        }
    }

    private fun detectLanguage() {
        val languageIdentifier = LanguageIdentification.getClient()

        languageIdentifier.identifyLanguage(ScanActivityUtils.extractedText).addOnSuccessListener {
            ScanActivityUtils.detectedLanguage = it
        }.addOnFailureListener {
            Log.e("LANG_DETECTION", it.message.toString())
            Toast.makeText(this, "Language detection failed!", Toast.LENGTH_SHORT).show()
        }
    }
}