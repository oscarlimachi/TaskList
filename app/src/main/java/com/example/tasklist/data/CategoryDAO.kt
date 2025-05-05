package com.example.tasklist.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tasklist.utils.DatabaseManager


class CategoryDAO (private val context: Context) {
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
        try {
            val values = ContentValues().apply {
                put(Category.COLUMN_NAME_TITLE, category.title)
            }
            val newRowId = db.insert(Category.TABLE_NAME,null,values)
            Log.i("DATABASE","Inserted in category with id: $newRowId")
        } catch (e: Exception){
            e.printStackTrace()
        }finally {
            close()
        }
    }
    //UPDATE
    fun update(category: Category){
        open()
        try {
            val values = ContentValues()
            values.put(Category.COLUMN_NAME_TITLE,category.title)
            //which row to update, based on the id
            val selection = "${Category.COLUMN_NAME_ID} = ${category.id}"

            val count = db.update(Category.TABLE_NAME,values,selection,null)
            Log.i("DATABASE","Update category with id: ${category.id}")

        } catch (e: Exception){
            e.printStackTrace()
        } finally {
            close()
        }
    }
    //DELETE
    fun delete(category: Category){
        open()
        try {
            val selection = "${Category.COLUMN_NAME_ID} = ${category.id}"

            val deletedRows = db.delete(Category.TABLE_NAME,selection,null)
            Log.i("DATABASE", "Deleted category with id: ${category.id}")
        } catch (e:Exception){
            e.printStackTrace()
        }finally {
            close()
        }
    }
    //TAKE BY ID
    fun findById(id:Long): Category?{
        open()
        var category: Category? = null
        try {
            //what column you want
            val projection = arrayOf(
                Category.COLUMN_NAME_ID,
                Category.COLUMN_NAME_TITLE
            )
            //filter where "id" = category.id
            val selection = "${Category.COLUMN_NAME_ID} = $id"
            val cursor = db.query(
                Category.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
            )
            if (cursor.moveToNext()){
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE))
                category = Category(id, title)
            }
            cursor.close()
        } catch (e:Exception){
            e.printStackTrace()
        } finally {
            close()
        }
        return category
    }
    //TAKE ALL FIELDS
    fun findAll(): List<Category>{
        open()
        var categoryList: MutableList<Category> = mutableListOf()
        try {
            //what column you want
            val projection = arrayOf(
                Category.COLUMN_NAME_ID,
                Category.COLUMN_NAME_TITLE
            )
            //filter where "id" = category.id
            val selection = null
            val cursor = db.query(
                Category.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
            )
            while (cursor.moveToNext()){
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE))
                val category = Category(id, title)
                categoryList.add(category)
            }
            cursor.close()
        } catch (e:Exception){
            e.printStackTrace()
        } finally {
            close()
        }
        return categoryList
    }

}