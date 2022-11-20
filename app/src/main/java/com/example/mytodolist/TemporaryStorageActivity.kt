package com.example.mytodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodolist.Adapter.TemporaryStorageAdapter
import com.example.mytodolist.databinding.ActivityTemporaryStorageBinding
import com.example.mytodolist.model.TodoListData

class TemporaryStorageActivity : AppCompatActivity() {

    private lateinit var temporaryStorageBinding: ActivityTemporaryStorageBinding
    private var temporaryStorageAdapter : TemporaryStorageAdapter? = null
    private var tempStorage = mutableListOf<TodoListData?>()
    private var manager : LinearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        temporaryStorageBinding = ActivityTemporaryStorageBinding.inflate(layoutInflater)
        setContentView(temporaryStorageBinding.root)

        initStorageRecyclerView()

        temporaryStorageBinding.testData.setOnClickListener {
            tempStorage.clear()

            temporaryStorageAdapter!!.storageData = dataSet()

            temporaryStorageAdapter!!.notifyDataSetChanged()
        }


        if (temporaryStorageAdapter!!.storageData.size == 0) {
            println(temporaryStorageAdapter!!.storageData.size)
            temporaryStorageBinding.nullTv.visibility = View.VISIBLE
        } else {
            temporaryStorageBinding.nullTv.visibility = View.GONE
        }
    }

    private fun dataSet(): MutableList<TodoListData?> {
        tempStorage = mutableListOf(
            TodoListData(1,"test1",false),
            TodoListData(2,"test2",false),
            TodoListData(3,"test3",false),
            TodoListData(4,"test4",false),
            TodoListData(5,"test5",false),
            TodoListData(6,"test6",false),
            TodoListData(7,"test7",false),
            TodoListData(8,"test8",false)
        )
        return tempStorage
    }

    private fun initStorageRecyclerView() {
        temporaryStorageAdapter = TemporaryStorageAdapter()
        //tempStorage = temporaryStorageAdapter!!.storageData
        temporaryStorageAdapter!!.storageData = tempStorage
        temporaryStorageBinding.temporaryStorageRecyclerview.adapter = temporaryStorageAdapter
        temporaryStorageBinding.temporaryStorageRecyclerview.layoutManager = manager
    }
}