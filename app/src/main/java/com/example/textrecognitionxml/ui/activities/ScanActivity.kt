package com.example.textrecognitionxml.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.createBitmap
import androidx.core.view.isVisible
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.utils.ContextExtensions
import com.example.textrecognitionxml.utils.ScanActivityUtils
import com.example.textrecognitionxml.views.TextArea
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScanActivity : AppCompatActivity() {
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
        val svTextAreaContainer: ScrollView = findViewById(R.id.svTextAreaContainer)

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
            if (!svTextAreaContainer.isVisible) {
                fabEdit.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done))

                tvExtractedText.visibility = View.INVISIBLE
                svTextAreaContainer.visibility = View.VISIBLE

                etEditor.setText(ScanActivityUtils.extractedText, TextView.BufferType.EDITABLE)

                state = 1
            } else {
                fabEdit.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))

                ScanActivityUtils.extractedText = etEditor.text.toString()
                tvExtractedText.text = ScanActivityUtils.extractedText

                svTextAreaContainer.visibility = View.INVISIBLE
                tvExtractedText.visibility = View.VISIBLE

                if (state == 1) {
                    extensions.hideKeyboard(fabEdit)
                }

                state = 0
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ScanActivityUtils.run {
            extractedText = ""
            bitmap = createBitmap(1, 1)
        }
    }
}