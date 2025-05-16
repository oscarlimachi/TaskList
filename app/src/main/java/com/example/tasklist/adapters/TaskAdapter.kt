package com.example.tasklist.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tasklist.data.Task
import com.example.tasklist.databinding.ItemTaskBinding

class TaskAdapter(
    var items: List<Task>,
    val onItemClick: (position: Int) -> Unit,
    val onItemCheck: (position: Int) -> Unit,
    val onItemMenu: (position: Int, v: View) -> Unit

    ): Adapter<TaskViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
       val task = items[position]
        holder.render(task)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
        holder.binding.doneCheckBox.setOnCheckedChangeListener { compoundButton, b ->
            if (holder.binding.doneCheckBox.isPressed){
                onItemCheck(position)
            }
        }
        holder.binding.menuButton.setOnClickListener {v ->
            onItemMenu(position, v)
        }

    }
    fun updateItems(items: List<Task>){
        this.items=items
        notifyDataSetChanged()
    }
}
class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root){
    fun render(task: Task){
        binding.TaskTextView.text = task.title
        binding.doneCheckBox.isChecked = task.done
    }
}