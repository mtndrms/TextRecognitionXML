package com.example.textrecognitionxml.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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
import com.google.android.material.textfield.TextInputLayout
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.*

class ScanResultActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_result)

        // 0 = read mode
        // 1 = edit mode
        var state = 0

        val extensions = ContextExtensions(this)

        val tvExtractedText: TextView = findViewById(R.id.tvExtractedText)
        val ivClose: ImageView = findViewById(R.id.ivClose)
        val ivShare: ImageView = findViewById(R.id.ivShare)
        val ivTranslate: ImageView = findViewById(R.id.ivTranslate)
        val fabEdit: FloatingActionButton = findViewById(R.id.fabEdit)
        val etEditor: TextArea = findViewById(R.id.etEditor)
        val ddTargetLanguages: TextInputLayout = findViewById(R.id.ddTargetLanguages)

        tvExtractedText.movementMethod = ScrollingMovementMethod()
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

        ivTranslate.setOnClickListener {
            ddTargetLanguages.run {
                visibility = if (visibility == View.GONE) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
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
    }

    override fun onResume() {
        super.onResume()

        val tvAutoCompleteTextView: AutoCompleteTextView = findViewById(R.id.tvAutoCompleteTextView)
        val ddTargetLanguages: TextInputLayout = findViewById(R.id.ddTargetLanguages)
        val languages = ScanActivityUtils.languages.values.toMutableList()
        val arrayAdapter = ArrayAdapter(
            this,
            R.layout.item_dropdown_language,
            languages
        )
        tvAutoCompleteTextView.setAdapter(arrayAdapter)

        tvAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            ScanActivityUtils.detectedLanguage?.let {
                translate(
                    it,
                    ScanActivityUtils.languages.keys.toList()[position]
                )
                ddTargetLanguages.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ScanActivityUtils.run {
            extractedText = ""
            bitmap = createBitmap(1, 1)
            detectedLanguage = null
        }
    }

    private fun translate(source: String, target: String) {
        val tvExtractedText: TextView = findViewById(R.id.tvExtractedText)

        val options =
            TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.fromLanguageTag(source).toString())
                .setTargetLanguage(TranslateLanguage.fromLanguageTag(target).toString())
                .build()

        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder().requireWifi().build()
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(ScanActivityUtils.extractedText)
                    .addOnSuccessListener {
                        tvExtractedText.text = it
                    }.addOnFailureListener {
                        Log.e("TRANSLATE", it.message.toString())
                        Toast.makeText(this, "Translation failed!", Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener {
                Log.e("DOWN_MODEL", it.message.toString())
                Toast.makeText(this, "Failed while downloading model!", Toast.LENGTH_SHORT).show()
            }
        lifecycle.addObserver(translator)
    }
}