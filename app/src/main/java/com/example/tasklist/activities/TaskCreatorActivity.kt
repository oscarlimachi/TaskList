package com.example.tasklist.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tasklist.R
import com.example.tasklist.data.Category
import com.example.tasklist.data.CategoryDAO
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityTaskCreatorBinding


class TaskCreatorActivity : AppCompatActivity() {
    lateinit var binding: ActivityTaskCreatorBinding

    lateinit var task: Task
    lateinit var category: Category
    lateinit var taskDAO: TaskDAO
    lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityTaskCreatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)

        val categoryId = intent.getLongExtra("CATEGORY_ID",-1)
        category = categoryDAO.findById(categoryId)!!
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            task=Task(-1,title,false,category)
            taskDAO.insert(task)
            finish()
        }



    }
}