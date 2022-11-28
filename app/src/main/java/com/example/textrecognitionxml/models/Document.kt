package com.example.textrecognitionxml.models

import java.util.Calendar
import java.util.Date

data class Document(
    val id: Int,
    val folderId: Int,
    val title: String,
    val context: String,
    val createdAt: Date = Calendar.getInstance().time
)
