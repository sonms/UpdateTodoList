package com.example.mytodolist.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mytodolist.R
import com.example.mytodolist.databinding.ItemScheduleBinding
import com.example.mytodolist.databinding.TodoItemBinding
import com.example.mytodolist.databinding.ViewitemScheduleBinding
import com.example.mytodolist.model.ScheduleData

class ScheduleAdapter : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>(){
    private lateinit var binding : ViewitemScheduleBinding
    //page마다 값이 달라져야하니 , map으로 해서 key값으로 page, value로 tv1~4개가 맞는듯?
    var scheduleData = mutableListOf<ScheduleData?>()
    private lateinit var context : Context
    var dayData = mutableListOf<String>()

    inner class ScheduleViewHolder(private val binding : ViewitemScheduleBinding ) : RecyclerView.ViewHolder(binding.root) {
        private var position : Int? = null
        private val setDate : String = ""

        fun bind(scheduleData: ScheduleData, position : Int) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        context = parent.context
        binding = ViewitemScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScheduleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        //dayDate에 비해 scheduleData는 값(사이즈)이 더 없을 수 있으니 null처리해야할듯?

        //holder.bind(scheduleData[position]!!, position)
        /*if (position == dayData.size - 1) {
            viewPager2!!.post(runnable)
        }*/
    }

    //뷰페이저 수
   override fun getItemCount(): Int {
        return scheduleData.size
    }



}