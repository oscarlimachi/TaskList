package com.example.tasklist.data

class Task(
    val id: Long,
    val title: String,
    val done: Boolean,
    val category: Category
){
    companion object{
        const val TABLE_NAME = "Tasks"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DONE = "done"
        const val COLUMN_NAME_CATEGORY = "category_id"
    }
}