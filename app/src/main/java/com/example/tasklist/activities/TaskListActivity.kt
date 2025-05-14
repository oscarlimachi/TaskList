package com.example.tasklist.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Adapter
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tasklist.R
import com.example.tasklist.adapters.TaskAdapter
import com.example.tasklist.data.Category
import com.example.tasklist.data.CategoryDAO
import com.example.tasklist.data.Task
import com.example.tasklist.data.TaskDAO
import com.example.tasklist.databinding.ActivityListTaskBinding

class TaskListActivity : AppCompatActivity() {
    lateinit var binding: ActivityListTaskBinding
    lateinit var categoryDAO: CategoryDAO
    lateinit var category: Category

    lateinit var taskDAO: TaskDAO
    lateinit var taskList: List<Task>

    lateinit var adapter: TaskAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Inicializar
        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)

        val id = intent.getLongExtra("CATEGORY_ID",-1)
        category = categoryDAO.findById(id)!!
        taskList = taskDAO.findByCategoryId(category)

        adapter = TaskAdapter(taskList, {
            //click on item
        }, {
                //click on checkbox
        })
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        supportActionBar?.title = category.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
       }
    //button for back
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}