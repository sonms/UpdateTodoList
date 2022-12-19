package com.example.mytodolist.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.R
import com.example.mytodolist.databinding.ItemScheduleBinding
import com.example.mytodolist.databinding.SearchItemLayoutBinding
import com.example.mytodolist.databinding.TodoItemBinding
import com.example.mytodolist.databinding.ViewitemScheduleBinding
import com.example.mytodolist.model.ScheduleData
import com.example.mytodolist.model.SearchData
import com.example.mytodolist.model.TodoListData

class SearchItemAdapter : RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder>(){
    private lateinit var binding : SearchItemLayoutBinding
    var searchItemData = mutableListOf<SearchData?>()
    private lateinit var context : Context

    inner class SearchItemViewHolder(private val binding : SearchItemLayoutBinding ) : RecyclerView.ViewHolder(binding.root) {
        private var position : Int? = null
        private val setDate : String = ""
        var tv_item = binding.searchItemTv
        fun bind(searchItemData: SearchData, position : Int) {
            this.position = position
            tv_item.text = searchItemData.searchItemText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        context = parent.context
        binding = SearchItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        holder.bind(searchItemData[position]!!, position)
        val content : SearchData = searchItemData[position]!!
        //holder.tv_date.text = content.scheduleText
    }

    override fun getItemCount(): Int {
        return searchItemData.size
    }

}