package com.example.textrecognitionxml.models

data class Folder(
    val id: Int,
    val name: String,
    var documents: MutableList<Document> = mutableListOf(),
    var size: Int = 0
)
