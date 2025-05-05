package com.example.tasklist.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tasklist.data.Category

class DatabaseManager(context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
) {
    companion object {
        const val DATABASE_VERSION=1
        const val DATABASE_NAME="toDoList.db"
        private const val DATABASE_CREATE_CATEGORY =
            "CREATE TABLE ${Category.TABLE_NAME} "+
                    "(${Category.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    "${Category.COLUMN_NAME_TITLE} TEXT)"
        private const val DATABASE_DELETE_CATEGORY = "DROP TABLE IF EXISTS ${Category.TABLE_NAME}"
    }
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DATABASE_CREATE_CATEGORY)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }
    private fun onDestroy(db: SQLiteDatabase){
        db.execSQL(DATABASE_DELETE_CATEGORY)
    }





}