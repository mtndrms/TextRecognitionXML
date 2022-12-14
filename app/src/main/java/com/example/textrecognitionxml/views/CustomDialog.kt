package com.example.textrecognitionxml.views

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.services.FolderService

class CustomDialog(private var c: Activity) : Dialog(c), View.OnClickListener {
    private var d: Dialog? = null

    private lateinit var etFolderName: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_create_folder)

        etFolderName = findViewById(R.id.etFolderName)
        val btnCreate: AppCompatButton = findViewById(R.id.btnCreate)
        val btnCancel: AppCompatButton = findViewById(R.id.btnCancel)

        btnCreate.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnCreate -> {
                createNewFolderOnDatabase(etFolderName.text.toString())
                d?.dismiss()
            }
            R.id.btnCancel -> {
                dismiss()
            }
            else -> {}
        }
        dismiss()
    }

    private fun createNewFolderOnDatabase(name: String) {
        val folderService = FolderService(c)
        folderService.createFolder(name)
    }
}