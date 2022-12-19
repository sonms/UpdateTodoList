package com.example.mytodolist.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.R
import com.example.mytodolist.databinding.ItemScheduleBinding
import com.example.mytodolist.databinding.SearchWordLayoutBinding
import com.example.mytodolist.databinding.TodoItemBinding
import com.example.mytodolist.databinding.ViewitemScheduleBinding
import com.example.mytodolist.model.ScheduleData
import com.example.mytodolist.model.SearchWordData
import com.example.mytodolist.model.TodoListData

class SearchWordAdapter : RecyclerView.Adapter<SearchWordAdapter.SearchWordViewHolder>(){
    private lateinit var binding : SearchWordLayoutBinding

    var searchWordData = mutableListOf<SearchWordData?>()

    private lateinit var context : Context

    inner class SearchWordViewHolder(private val binding : SearchWordLayoutBinding ) : RecyclerView.ViewHolder(binding.root) {
        private var position : Int? = null
        var searchWord_tv = binding.searchWordTv
        fun bind(searchWordData: SearchWordData, position : Int) {
            this.position = position
            searchWord_tv.text = searchWordData.searchWordText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchWordViewHolder {
        context = parent.context
        binding = SearchWordLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchWordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchWordAdapter.SearchWordViewHolder, position: Int) {
        holder.bind(searchWordData[position]!!, position)
        val content : SearchWordData = searchWordData[position]!!
        holder.searchWord_tv.text = content!!.searchWordText
    }

    override fun getItemCount(): Int {
        return searchWordData.size
    }

}