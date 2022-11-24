package com.example.mytodolist.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.databinding.TemporaryStorageItemBinding
import com.example.mytodolist.model.TodoListData


class TemporaryStorageAdapter : RecyclerView.Adapter<TemporaryStorageAdapter.TempViewHolder>() {

    private lateinit var temporaryStorageItemBinding: TemporaryStorageItemBinding
    private lateinit var context: Context
    var storageData = ArrayList<TodoListData?>()
    private var todoAdapter: TodoAdapter? = null

    inner class TempViewHolder(private val binding : TemporaryStorageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position : Int? = null
        private var tempContent = temporaryStorageItemBinding.tempContent
        private var tempRemove = temporaryStorageItemBinding.tempRemove

        fun bind(storageData: TodoListData, position : Int) {
            this.position = position
            tempContent.text = storageData.content

            /*if (isRemove) {
                tempRemove.setOnClickListener {
                    removeData(this.layoutPosition)
                }
            }*/
            tempRemove.setOnClickListener {
                removeData(this.layoutPosition)
                todoAdapter = TodoAdapter()
                todoAdapter!!.isRemove = true
                //새로운 어댑터 생성이라 데이터가 안넘어옴
                //println(todoAdapter!!.testData)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempViewHolder {
        context = parent.context
        temporaryStorageItemBinding = TemporaryStorageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TempViewHolder(temporaryStorageItemBinding)
    }

    override fun onBindViewHolder(holder: TempViewHolder, position: Int) {
        holder.bind(storageData[position]!!, position)
    }

    override fun getItemCount(): Int {
        return storageData.size
    }

    //////////데이터 handle
    fun removeData(position: Int) {
        storageData.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addData(itemData : TodoListData) {
        storageData.add(itemData)
        notifyDataSetChanged()
    }
}