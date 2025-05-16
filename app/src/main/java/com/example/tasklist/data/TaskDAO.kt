package com.example.tasklist.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.tasklist.utils.DatabaseManager


class TaskDAO(private val context: Context) {
    private lateinit var db: SQLiteDatabase
    private fun open(){
        db = DatabaseManager(context).writableDatabase
    }
    private fun close(){
        db.close()
    }
    //create actions

    //Insert
    fun insert(task: Task){
        open()
        try {
            val values = ContentValues().apply {
                put(Task.COLUMN_NAME_TITLE,task.title)
                put(Task.COLUMN_NAME_DONE,task.done)
                put(Task.COLUMN_NAME_CATEGORY,task.category.id)
            }
            val newRowId = db.insert(Task.TABLE_NAME,null,values)
            Log.i("DATABASE", "Inserted a task with id: $newRowId")
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            close()
        }

    }
    //Update
    fun update(task: Task){
        open()
        try {
            val values = ContentValues().apply {
                put(Task.COLUMN_NAME_TITLE, task.title)
                put(Task.COLUMN_NAME_DONE,task.done)
                put(Task.COLUMN_NAME_CATEGORY,task.category.id)
            }
            //which row to update, based on the id
            val selection= "${Task.COLUMN_NAME_ID} = ${task.id}"
            val count = db.update(Task.TABLE_NAME,values,selection,null)
            Log.i("DATABASE", "Update task with id: ${task.id}")
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            close()
        }

    }
    //Delete
    fun delete(task: Task){
        open()
        try {
            val selection = "${Task.COLUMN_NAME_ID}=${task.id}"
            val deleteRows = db.delete(Task.TABLE_NAME,selection,null)
            Log.i("DATABASE", "Delete task with id: ${task.id}")
        }catch (e: Exception){
            e.printStackTrace()
        } finally {
            close()
        }

    }
    //Find by Id
    fun findById(id: Long) : Task?{
        open()
        var task: Task? = null

    try {
        val projection = arrayOf(
            Task.COLUMN_NAME_ID,
            Task.COLUMN_NAME_TITLE,
            Task.COLUMN_NAME_DONE,
            Task.COLUMN_NAME_CATEGORY)

        val selection ="${Task.COLUMN_NAME_ID} = $id"
        val cursor = db.query(
            Task.TABLE_NAME,
            projection,
            selection,
            null,
            null,
            null,
            null)
        if (cursor.moveToNext()){
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) != 0
            val categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))

            val category = CategoryDAO(context).findById(categoryId)!!
            task = Task(id, title, done, category)
        }
        cursor.close()
    } catch (e:Exception){
        e.printStackTrace()
    } finally {
        close()
    }

        return task
    }
    //Find all
    fun findAll(): List<Task>{
        open()
        val taskList: MutableList<Task> = mutableListOf()
        try {

            val projection = arrayOf(
                Task.COLUMN_NAME_ID,
                Task.COLUMN_NAME_TITLE,
                Task.COLUMN_NAME_DONE,
                Task.COLUMN_NAME_CATEGORY
            )
            //val selection ="id = task.id
            val selection = null
            val cursor = db.query(
                Task.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null,)
            while (cursor.moveToNext()){
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) !=0
                val categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))

                val category = CategoryDAO(context).findById(categoryId)!!
                val task = Task(id,title,done,category)
                taskList.add(task)
            }
            cursor.close()
        } catch (e:Exception){
            e.printStackTrace()
        } finally {
            close()
        }
        return taskList
    }
    fun findAllByCategoryId(category: Category): List<Task>{
        open()
        val taskList: MutableList<Task> = mutableListOf()
        try {

            val projection = arrayOf(
                Task.COLUMN_NAME_ID,
                Task.COLUMN_NAME_TITLE,
                Task.COLUMN_NAME_DONE,
                Task.COLUMN_NAME_CATEGORY
            )
            //val selection ="id = task.id
            val selection = "${Task.COLUMN_NAME_CATEGORY}=${category.id}"
            val cursor = db.query(
                Task.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                Task.COLUMN_NAME_DONE)
            while (cursor.moveToNext()){
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_TITLE))
                val done = cursor.getInt(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_DONE)) !=0
                val categoryId = cursor.getLong(cursor.getColumnIndexOrThrow(Task.COLUMN_NAME_CATEGORY))

                val category = CategoryDAO(context).findById(categoryId)!!
                val task = Task(id,title,done,category)
                taskList.add(task)
            }
            cursor.close()
        } catch (e:Exception){
            e.printStackTrace()
        } finally {
            close()
        }
        return taskList
    }



}