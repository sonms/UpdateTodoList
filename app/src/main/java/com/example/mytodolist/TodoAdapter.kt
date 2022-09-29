package com.example.mytodolist

import android.content.ClipData
import android.content.ClipData.Item
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.databinding.TodoItemBinding

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {
    private lateinit var todoItemBinding: TodoItemBinding
    var listData = mutableListOf<TodoListData>()
    private var checkBoxStatus = mutableListOf<CheckBoxData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        var inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        todoItemBinding = TodoItemBinding.inflate(inflater, parent, false)

        return TodoViewHolder(todoItemBinding)
    }
    //checkboxstatus에서 indexoutofboundsexception
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(listData[position],checkBoxStatus[position], position)
    }

    override fun getItemCount(): Int {
        return listData.size
    }



    inner class TodoViewHolder(var todoItemBinding: TodoItemBinding) : RecyclerView.ViewHolder(todoItemBinding.root) {
        private var position : Int? = null
        private var ckID = 0
        var checkBox : CheckBox = todoItemBinding.checkBox
        //usercheckstatus = checkboxdata , user = todolistdata
        //checkboxuser = checkbox
        fun bind(data : TodoListData, checkData : CheckBoxData, position: Int) {
            this.position = position
            todoItemBinding.todoText.text = data.content

            checkBox.isChecked = checkData.checked

            checkBox.setOnClickListener {
                checkData.checked = checkBox.isChecked
                notifyItemChanged(adapterPosition)
            }
        }
    }
}