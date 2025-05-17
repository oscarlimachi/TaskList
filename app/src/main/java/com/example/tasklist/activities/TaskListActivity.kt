package com.example.tasklist.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
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
        taskList = taskDAO.findAllByCategoryId(category)

        adapter = TaskAdapter(taskList, {
            position ->
            val task = taskList[position]
            val intent = Intent(this, TaskCreatorActivity::class.java)
            intent.putExtra("CATEGORY_ID",category.id)
            intent.putExtra("TASK_ID",task.id)
            startActivity(intent)
        },
            { position ->
            val task = taskList[position]
            task.done = !task.done
            taskDAO.update(task)
            reloadData()

        }, {
          // si le da a los 3 puntos que hace
            position, v ->
            val popupMenu = PopupMenu(this,v)
            popupMenu.inflate(R.menu.task_context_menu)
            //id de las 2 opciones
            popupMenu.setOnMenuItemClickListener{ menuItem : MenuItem ->
                val task = taskList[position]
                return@setOnMenuItemClickListener when(menuItem.itemId){
                    R.id.actionEdit ->{
                        val intent = Intent(this, TaskCreatorActivity::class.java)
                        intent.putExtra("CATEGORY_ID",category.id)
                        intent.putExtra("TASK_ID",task.id)
                        startActivity(intent)
                        true}
                    R.id.actionDelete ->{
                        Toast.makeText(this,"Borrar", Toast.LENGTH_SHORT).show()
                        taskDAO.delete(task)
                        reloadData()
                        true}
                    else -> super.onOptionsItemSelected(menuItem)
                }
            }
                //if you want a red icon change to android -popupmenu
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    //show the icon
                    popupMenu.setForceShowIcon(true)
                }

                popupMenu.show()

        })
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        supportActionBar?.title = category.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //button to make task
        binding.addTaskButton.setOnClickListener{
            val intent = Intent(this, TaskCreatorActivity::class.java)
            intent.putExtra("CATEGORY_ID",category.id)
            startActivity(intent)
        }
       }

    //reload recycler
    override fun onResume() {
        super.onResume()
        reloadData()
    }
    //reload task list
    fun reloadData(){
        taskList = taskDAO.findAllByCategoryId(category)
        adapter.updateItems(taskList)
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
    //floating menu
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.task_context_menu,menu)
    }

}