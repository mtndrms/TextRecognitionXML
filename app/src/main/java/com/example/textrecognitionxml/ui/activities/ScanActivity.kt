package com.example.textrecognitionxml.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.utils.ContextExtensions
import com.example.textrecognitionxml.utils.ScanActivityUtils
import com.example.textrecognitionxml.views.TextArea
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.*

class ScanActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        // 0 = read mode
        // 1 = edit mode
        var state = 0

        val extensions = ContextExtensions(this)

        val ivImage: ImageView = findViewById(R.id.ivImage)
        val tvExtractedText: TextView = findViewById(R.id.tvExtractedText)
        val ivClose: ImageView = findViewById(R.id.ivClose)
        val ivShare: ImageView = findViewById(R.id.ivShare)
        val fabEdit: FloatingActionButton = findViewById(R.id.fabEdit)
        val etEditor: TextArea = findViewById(R.id.etEditor)

        tvExtractedText.movementMethod = ScrollingMovementMethod()

        ivImage.setImageBitmap(ScanActivityUtils.bitmap)
        tvExtractedText.text = ScanActivityUtils.extractedText

        ivClose.setOnClickListener {
            finish()
        }

        ivShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, ScanActivityUtils.extractedText)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        fabEdit.setOnClickListener {
            if (state == 0) {
                fabEdit.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done))

                tvExtractedText.visibility = View.INVISIBLE
                etEditor.visibility = View.VISIBLE

                etEditor.setText(ScanActivityUtils.extractedText, TextView.BufferType.EDITABLE)

                state = 1
            } else {
                fabEdit.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))

                ScanActivityUtils.extractedText = etEditor.text.toString()
                tvExtractedText.text = ScanActivityUtils.extractedText

                etEditor.visibility = View.INVISIBLE
                tvExtractedText.visibility = View.VISIBLE

                if (state == 1) {
                    extensions.hideKeyboard(fabEdit)
                }

                state = 0
            }
        }

        launch {
            extractedText(bitmap = ScanActivityUtils.bitmap)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ScanActivityUtils.run {
            extractedText = ""
            bitmap = createBitmap(1, 1)
        }
    }

    private suspend fun extractedText(bitmap: Bitmap) {
        val recognizer: TextRecognizer =
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val inputImage = InputImage.fromBitmap(bitmap, 0)
        val tvExtractedText: TextView = findViewById(R.id.tvExtractedText)
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
                        tvExtractedText.text = it.text
                    }
                }
                timer.start()
            }
        }
    }
}