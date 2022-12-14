package com.example.textrecognitionxml.services

import android.content.ContentValues
import android.content.Context
import com.example.textrecognitionxml.models.Document
import com.example.textrecognitionxml.models.Folder
import com.example.textrecognitionxml.utils.DatabaseHelper

class FolderService(private val context: Context) {
    private val db = DatabaseHelper(context, null)
    private lateinit var folder: Folder

    fun createFolder(name: String) {
        val values = ContentValues()
        values.put(DatabaseHelper.F_NAME_COL, name)

        db.writableDatabase.insert(DatabaseHelper.F_TABLE_NAME, null, values)
        db.writableDatabase.close()
    }

    fun getAllFolders(): List<Folder> {
        val cursor =
            db.readableDatabase.rawQuery("SELECT * FROM ${DatabaseHelper.F_TABLE_NAME}", null)
        cursor.close()

        return emptyList()
    }

    private fun getFolderById(folderId: Int): Folder {
        val cursor = db.readableDatabase.rawQuery(
            "SELECT * FROM ${DatabaseHelper.F_TABLE_NAME} WHERE id = $folderId",
            null
        )

        val idColumnId = if (cursor.getColumnIndex(DatabaseHelper.F_ID_COL) >= 0) {
            cursor.getColumnIndex(DatabaseHelper.F_ID_COL)
        } else {
            0
        }

        val nameColumnId = if (cursor.getColumnIndex(DatabaseHelper.F_NAME_COL) >= 0) {
            cursor.getColumnIndex(DatabaseHelper.F_NAME_COL)
        } else {
            0
        }

        while (cursor.moveToNext()) {
            folder = Folder(cursor.getInt(idColumnId), cursor.getString(nameColumnId))
        }
        cursor.close()
        return folder
    }

    fun getFolderByIdWithItsDocuments(folderId: Int): Folder {
        val documentService = DocumentService(context)
        val foldersDocuments: MutableList<Document> = documentService.getDocumentsByTheirFolder(folderId)

        val folder = getFolderById(folderId)
        folder.documents = foldersDocuments
        folder.size = foldersDocuments.size

        return folder
    }

    fun getAllFoldersWithTheirDocuments(): List<Folder> {
        val cursor = db.readableDatabase.rawQuery(
            "SELECT * FROM ${DatabaseHelper.F_TABLE_NAME}",
            null
        )

        val idColumnId = if (cursor.getColumnIndex(DatabaseHelper.F_ID_COL) >= 0) {
            cursor.getColumnIndex(DatabaseHelper.F_ID_COL)
        } else {
            0
        }

        val nameColumnId = if (cursor.getColumnIndex(DatabaseHelper.F_NAME_COL) >= 0) {
            cursor.getColumnIndex(DatabaseHelper.F_NAME_COL)
        } else {
            0
        }

        val documentService = DocumentService(context)
        val folders: MutableList<Folder> = mutableListOf()
        while (cursor.moveToNext()) {
            val folder = Folder(
                cursor.getInt(idColumnId),
                cursor.getString(nameColumnId)
            )
            folder.documents = documentService.getDocumentsByTheirFolder(cursor.getInt(idColumnId))
            folders.add(folder)
        }
        cursor.close()

        return folders
    }
}