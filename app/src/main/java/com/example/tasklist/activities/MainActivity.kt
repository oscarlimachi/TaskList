package com.example.tasklist.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasklist.adapters.CategoryAdapter
import com.example.tasklist.R
import com.example.tasklist.data.Category
import com.example.tasklist.data.CategoryDAO
import com.example.tasklist.databinding.ActivityMainBinding
import com.example.tasklist.databinding.DialogCreateCategoryBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: CategoryAdapter
    var categoryList: List<Category> = emptyList()
    lateinit var categoryDAO: CategoryDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryDAO = CategoryDAO(this)
        categoryList = categoryDAO.findAll()

        adapter = CategoryAdapter(categoryList, {
            //push category
        }, {
            //push edit
        }, {
            //push delete
        })
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.addCategoryButton.setOnClickListener {
            showCategoryDialog()
        }

    }


    fun showCategoryDialog() {
        val dialogBinding = DialogCreateCategoryBinding.inflate(layoutInflater)

        MaterialAlertDialogBuilder(this)
            .setTitle("Create Category")
            .setView(dialogBinding.root)
            .setPositiveButton(android.R.string.ok, {dialog, which ->
                val title = dialogBinding.titleEditText.text.toString()
                val category = Category(-1,title)
                categoryDAO.insert(category)
                loadData()
            })
            .setNegativeButton(android.R.string.cancel,null)
            .setIcon(R.drawable.ic_add)
            .show()

    }
    fun loadData(){
        categoryList = categoryDAO.findAll()
        adapter.updateItems(categoryList)
    }
}