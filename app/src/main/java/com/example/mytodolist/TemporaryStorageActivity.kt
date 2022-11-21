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
    private var tempItem : ArrayList<TodoListData?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        temporaryStorageBinding = ActivityTemporaryStorageBinding.inflate(layoutInflater)
        setContentView(temporaryStorageBinding.root)

        val type = intent.getStringExtra("type")


        initStorageRecyclerView()

        //받는거 하나 보여주는 거 하나 따로..?
        if (type.equals("delete")){
            tempItem = intent.getSerializableExtra("item") as ArrayList<TodoListData?>
            tempStorage.addAll(tempItem)
        }

        temporaryStorageBinding.testData.setOnClickListener {
            tempStorage.clear()

            //temporaryStorageAdapter!!.storageData = dataSet()

            temporaryStorageAdapter!!.notifyDataSetChanged()
        }


        if (temporaryStorageAdapter!!.itemCount == 0) {
            temporaryStorageBinding.nullTv.visibility = View.VISIBLE
        } else {
            println(temporaryStorageAdapter!!.itemCount)
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