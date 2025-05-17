package com.example.tasklist.activities


import android.os.Bundle
import android.view.MenuItem

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

        val id = intent.getLongExtra("TASK_ID",-1)
        category = categoryDAO.findById(categoryId)!!

        if (id == -1L){
            task = Task(-1L,"",false,category)
        } else{
            task = taskDAO.findById(id)!!
        }

        binding.titleTaskEditText.setText(task.title)

        binding.saveButton.setOnClickListener {
            val title = binding.titleTaskEditText.text.toString()
            task.title = title

            if (task.id == -1L){
                taskDAO.insert(task)

            } else{
                taskDAO.update(task)
            }
            finish()
        }
        supportActionBar?.title = category.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}
