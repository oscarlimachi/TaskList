package com.example.tasklist

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class CategoryDAO (val context: Context) {
   private lateinit var db: SQLiteDatabase

    //open of modification of database
   private fun open(){
        db = DatabaseManager(context).writableDatabase
    }
    //close of modification of database
   private fun close(){
        db.close()
    }
    //***Start the action of a database***

    //INSERT
    fun insert(category: Category){
        open()
        val contentValues = ContentValues().apply {
            put(Category.COLUMN_NAME_TITLE, category.title)
        }
    }


}