package com.example.textrecognitionxml.models

data class Folder(
    val id: Int,
    val name: String,
    val documents: MutableList<Document>,
)
