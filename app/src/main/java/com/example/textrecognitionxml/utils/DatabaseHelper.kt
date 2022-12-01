package com.example.textrecognitionxml.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "TEXT_RECOGNITION_APP"
        private const val DATABASE_VERSION = 1

        // Documents table's columns
        const val D_TABLE_NAME = "documents"
        const val D_ID_COL = "id"
        const val D_FOLDER_ID_COL = "folder_id"
        const val D_TITLE_COL = "title"
        const val D_DETAIL_COL = "detail"
        const val D_CREATION_DATE_COL = "created_at"

        // Folders table's columns
        const val F_TABLE_NAME = "folders"
        const val F_ID_COL = "id"
        const val F_NAME_COL = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val queryDocuments =
            (
                    "CREATE TABLE $D_TABLE_NAME" +
                            " (" +
                            "$D_ID_COL INTEGER PRIMARY KEY, " +
                            "$D_FOLDER_ID_COL INTEGER, " +
                            "$D_TITLE_COL TEXT, " +
                            "$D_DETAIL_COL TEXT, " +
                            "$D_CREATION_DATE_COL TEXT" +
                            ")"
                    )

        val queryFolder =
            (
                    "CREATE TABLE $F_TABLE_NAME" +
                            " (" +
                            "$F_ID_COL INTEGER PRIMARY KEY, " +
                            "$F_NAME_COL TEXT" +
                            ")"
                    )

        db.execSQL(queryDocuments)
        db.execSQL(queryFolder)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE If EXISTS $D_TABLE_NAME")
        db.execSQL("DROP TABLE IF EXISTS $F_TABLE_NAME")
        onCreate(db)
    }
}