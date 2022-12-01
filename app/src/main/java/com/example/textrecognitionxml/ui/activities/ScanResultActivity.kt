package com.example.textrecognitionxml.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.adapters.FoldersSpinnerAdapter
import com.example.textrecognitionxml.models.Folder
import com.example.textrecognitionxml.services.DocumentService
import com.example.textrecognitionxml.services.FolderService
import com.example.textrecognitionxml.utils.ContextExtensions
import com.example.textrecognitionxml.utils.DatabaseHelper
import com.example.textrecognitionxml.utils.ScanActivityUtils
import com.example.textrecognitionxml.views.CustomDialog
import com.example.textrecognitionxml.views.TextArea
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.util.*


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
        val ivSave: ImageView = findViewById(R.id.ivSaveDocument)
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

        ivSave.setOnClickListener {
            val contentView: View = View.inflate(
                this@ScanResultActivity, R.layout.fragment_bottomsheet_save_document, null
            )
            val dialog = BottomSheetDialog(this@ScanResultActivity)
            dialog.setContentView(contentView)
            dialog.show()

            val btnAdd: AppCompatButton = contentView.findViewById(R.id.btnAdd)
            val etDocumentTitle: EditText = contentView.findViewById(R.id.etDocumentTitle)
            val acFolders: AutoCompleteTextView =
                contentView.findViewById(R.id.tvAutoCompleteTextView)
            val btnCreateNewFolder: TextView = contentView.findViewById(R.id.btnCreateNewFolder)
            val folderService = FolderService(this)

            btnCreateNewFolder.setOnClickListener {
                dialog.dismiss()

                val cdd = CustomDialog(this)
                cdd.show()
            }

            var folderId = 1
            btnAdd.setOnClickListener {
                val title = etDocumentTitle.text.toString()
                val details = tvExtractedText.text.toString()
                val createdAt = Calendar.getInstance().time.toString()
                val documentService = DocumentService(this)

                documentService.createSingleDocument(folderId, title, details, createdAt)
                dialog.dismiss()
            }

            val folders = folderService.getAllFoldersWithTheirDocuments()
            val foldersSpinnerAdapter =
                FoldersSpinnerAdapter(
                    this,
                    R.layout.item_spinner_folder,
                    folders
                )
            acFolders.setAdapter(foldersSpinnerAdapter)
            acFolders.setSelection(0)

            acFolders.setOnItemClickListener { parent, view, position, id ->
                folderId = folders[position].id
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
            this, R.layout.item_dropdown_language, languages
        )
        tvAutoCompleteTextView.setAdapter(arrayAdapter)

        tvAutoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            ScanActivityUtils.detectedLanguage?.let {
                translate(
                    it, ScanActivityUtils.languages.keys.toList()[position]
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

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(source).toString())
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(target).toString()).build()

        val translator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder().requireWifi().build()
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener {
            translator.translate(ScanActivityUtils.extractedText).addOnSuccessListener {
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