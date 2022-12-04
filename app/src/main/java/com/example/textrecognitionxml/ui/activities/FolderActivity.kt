package com.example.textrecognitionxml.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.adapters.DocumentFolderRecyclerViewAdapter
import com.example.textrecognitionxml.models.Document
import com.example.textrecognitionxml.services.FolderService
import com.example.textrecognitionxml.utils.DatabaseHelper
import com.example.textrecognitionxml.utils.FakeDatabase

class FolderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        val rvDocuments: RecyclerView = findViewById(R.id.rvContents)
        val tvFolderName: TextView = findViewById(R.id.tvFolderName)

        val folderService = FolderService(this)
        val folder = folderService.getFolderByIdWithItsDocuments(
            intent.getIntExtra("folderId", 0)
        )

        val documentFolderRecyclerViewAdapter = DocumentFolderRecyclerViewAdapter(folder.documents)
        tvFolderName.text = folderService.getFolderByIdWithItsDocuments(folder.id).name

        rvDocuments.layoutManager = LinearLayoutManager(applicationContext)
        rvDocuments.adapter = documentFolderRecyclerViewAdapter
    }
}