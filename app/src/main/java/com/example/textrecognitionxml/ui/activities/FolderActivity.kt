package com.example.textrecognitionxml.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.textrecognitionxml.R
import com.example.textrecognitionxml.adapters.DocumentFolderRecyclerViewAdapter
import com.example.textrecognitionxml.utils.FakeDatabase

class FolderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder)

        val rvDocuments: RecyclerView = findViewById(R.id.rvContents)
        val tvFolderName: TextView = findViewById(R.id.tvFolderName)
        val documentFolderRecyclerViewAdapter = DocumentFolderRecyclerViewAdapter(
            FakeDatabase.folders[intent.getIntExtra(
                "folderId",
                0
            )].documents
        )

        tvFolderName.text = FakeDatabase.folders[intent.getIntExtra("folderId", 0)].name

        rvDocuments.layoutManager = LinearLayoutManager(applicationContext)
        rvDocuments.adapter = documentFolderRecyclerViewAdapter
    }
}