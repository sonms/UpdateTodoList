package com.example.mytodolist.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.TodoInterface
import com.example.mytodolist.databinding.TemporaryStorageItemBinding
import com.example.mytodolist.model.MyResponse
import com.example.mytodolist.model.TodoListData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TemporaryStorageAdapter : RecyclerView.Adapter<TemporaryStorageAdapter.TempViewHolder>() {

    private lateinit var temporaryStorageItemBinding: TemporaryStorageItemBinding
    private lateinit var context: Context
    var storageDataList : MutableList<TodoListData?> = mutableListOf()
    var trashDataList : MutableList<TodoListData?> = mutableListOf()
    var deleteTempServerData : String? = null
    private var todoAdapter: TodoAdapter? = null

    //데이터 연결
    val retrofit = Retrofit.Builder().baseUrl("https://waffle.gq")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(TodoInterface::class.java)

    inner class TempViewHolder(private val binding : TemporaryStorageItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position : Int? = null
        private var tempContent = temporaryStorageItemBinding.tempContent
        //private var tempRemove = temporaryStorageItemBinding.tempRemove

        fun bind(storageData: TodoListData, position : Int) {
            this.position = position
            tempContent.text = storageData.content

            /*if (isRemove) {
                tempRemove.setOnClickListener {
                    removeData(this.layoutPosition)
                }
            }*/

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempViewHolder {
        context = parent.context
        temporaryStorageItemBinding = TemporaryStorageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TempViewHolder(temporaryStorageItemBinding)
    }

    override fun onBindViewHolder(holder: TempViewHolder, position: Int) {
        holder.bind(storageDataList[holder.adapterPosition]!!, position)

        temporaryStorageItemBinding.tempRemove.setOnClickListener {
            deleteTempServerData = storageDataList[holder.adapterPosition!!]!!.api_id
            val t = storageDataList[holder.adapterPosition]!!.api_id
            //todoAdapter = TodoAdapter()
            println("lp" + holder.adapterPosition)
            println("ss"+storageDataList.size)
            storageDataList.forEach { i ->
                println(i)
            }
            println("ts" + trashDataList.size)
            trashDataList.forEach { j ->
                println(j)
            }
            //새로운 어댑터 생성이라 데이터가 안넘어옴
            //println(todoAdapter!!.testData)
            //removeServerData(storageDataList[this.layoutPosition]!!.api_id)
            println("id"+storageDataList[holder.adapterPosition]!!.api_id)
            println("content"+storageDataList[holder.adapterPosition]!!.content)
            removeData(holder.adapterPosition)
            removeServerData(storageDataList[holder.adapterPosition]!!.api_id)
        }
    }

    override fun getItemCount(): Int {
        return storageDataList.size
    }

    //////////데이터 handle
    fun removeData(position: Int) {
        storageDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeServerData(server_id : String) {
        service.deleteData(server_id).enqueue(object : Callback<MyResponse> {
            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse?>) {
                if (response.isSuccessful) {
                    //통신 성공
                    println("Success")
                } else {
                    //통신 실패
                    println("Fail")
                }
            }

            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                println("Error" + t.message.toString())
            }
        })
    }

    fun addData(itemData : TodoListData) {
        storageDataList.add(itemData)
        notifyDataSetChanged()
    }
}