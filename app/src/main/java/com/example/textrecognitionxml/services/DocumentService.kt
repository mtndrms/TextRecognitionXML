package com.example.textrecognitionxml.services

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.textrecognitionxml.models.Document
import com.example.textrecognitionxml.utils.DatabaseHelper

class DocumentService(context: Context) {
    private val db = DatabaseHelper(context, null)

    fun createSingleDocument(folderId: Int, title: String, detail: String, createdAt: String) {
        val values = ContentValues()

        values.put(DatabaseHelper.D_FOLDER_ID_COL, folderId)
        values.put(DatabaseHelper.D_TITLE_COL, title)
        values.put(DatabaseHelper.D_DETAIL_COL, detail)
        values.put(DatabaseHelper.D_CREATION_DATE_COL, createdAt)

        db.writableDatabase.insert(DatabaseHelper.D_TABLE_NAME, null, values)
        db.writableDatabase.close()
    }

    fun getAllDocuments(): Cursor? {
        return db.readableDatabase.rawQuery("SELECT * FROM ${DatabaseHelper.D_TABLE_NAME}", null)
    }

    fun getDocumentsByTheirFolder(folderId: Int): MutableList<Document> {
        val cursor = db.readableDatabase.rawQuery(
            "SELECT * FROM ${DatabaseHelper.D_TABLE_NAME} WHERE folder_id = $folderId", null
        )

        val idColumnId = if (cursor.getColumnIndex(DatabaseHelper.D_ID_COL) >= 0) {
            cursor.getColumnIndex(DatabaseHelper.D_ID_COL)
        } else {
            0
        }

        val folderIdColumnId = if (cursor.getColumnIndex(DatabaseHelper.D_FOLDER_ID_COL) >= 0) {
            cursor.getColumnIndex(DatabaseHelper.D_FOLDER_ID_COL)
        } else {
            0
        }

        val titleColumnId = if (cursor.getColumnIndex(DatabaseHelper.D_TITLE_COL) >= 0) {
            cursor.getColumnIndex(DatabaseHelper.D_TITLE_COL)
        } else {
            0
        }

        val detailColumnId = if (cursor.getColumnIndex(DatabaseHelper.D_DETAIL_COL) >= 0) {
            cursor.getColumnIndex(DatabaseHelper.D_DETAIL_COL)
        } else {
            0
        }

        val foldersDocuments: MutableList<Document> = mutableListOf()
        while (cursor.moveToNext()) {
            foldersDocuments.add(
                Document(
                    cursor.getInt(idColumnId),
                    cursor.getInt(folderIdColumnId),
                    cursor.getString(titleColumnId),
                    cursor.getString(detailColumnId),
                )
            )
        }

        println(foldersDocuments)
        cursor.close()

        return foldersDocuments
    }
}