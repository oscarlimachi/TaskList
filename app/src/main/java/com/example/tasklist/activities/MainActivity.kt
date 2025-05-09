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
            position ->
            val category = categoryList[position]
            showCategoryDialog(category)
        }, { //push delete
            position ->
            showDeleteConfirmationDialog(position)
        })
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.addCategoryButton.setOnClickListener {
            showCategoryDialog(Category(-1L,""))
        }



    }


    fun showCategoryDialog(category: Category) {
        val dialogBinding = DialogCreateCategoryBinding.inflate(layoutInflater)
        dialogBinding.titleEditText.setText(category.title)

        var dialogTitle = ""
        var dialogIcon = 0
        if (category.id != -1L){
            dialogTitle = "Edit Category"
            dialogIcon = R.drawable.ic_edit
        }else {
            dialogTitle = "Create Category"
            dialogIcon = R.drawable.ic_add
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(dialogTitle)
            .setView(dialogBinding.root)
            .setPositiveButton(android.R.string.ok, {dialog, which ->
                category.title = dialogBinding.titleEditText.text.toString()
                if (category.id !=-1L){
                    categoryDAO.update(category)
                }else{
                    categoryDAO.insert(category)
                }
                loadData()
            })
            .setNegativeButton(android.R.string.cancel,null)
            .setIcon(dialogIcon)
            .show()

    }
    fun showDeleteConfirmationDialog(position:Int){
        val category = categoryList[position]

        MaterialAlertDialogBuilder(this)
            .setTitle("Delete category")
            .setMessage("Are you sure?")
            .setPositiveButton(android.R.string.ok,{ dialog, which ->
                categoryDAO.delete(category)
                loadData()
            })
            .setNegativeButton(android.R.string.cancel, null)
            .setIcon(R.drawable.ic_delete)
            .show()
            }


    fun loadData(){
        categoryList = categoryDAO.findAll()
        adapter.updateItems(categoryList)
    }
}