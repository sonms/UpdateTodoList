package com.example.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodolist.Adapter.TemporaryStorageAdapter
import com.example.mytodolist.databinding.ActivityTemporaryStorageBinding
import com.example.mytodolist.model.TodoListData


class TemporaryStorageActivity : AppCompatActivity() {
    private val RESULT_TEST = 2
    private lateinit var temporaryStorageBinding: ActivityTemporaryStorageBinding
    private var temporaryStorageAdapter : TemporaryStorageAdapter? = null
    private var tempStorage = ArrayList<TodoListData?>()
    private var manager : LinearLayoutManager = LinearLayoutManager(this)
    private var tempItem : ArrayList<TodoListData?> = ArrayList()
    //뒤로가기 이벤트
    var backPressedTime : Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        temporaryStorageBinding = ActivityTemporaryStorageBinding.inflate(layoutInflater)
        setContentView(temporaryStorageBinding.root)

        val type = intent.getStringExtra("type")


        initStorageRecyclerView()

        //받는거 하나 보여주는 거 하나 따로..?->ArrayList 로 해결
        if (type.equals("delete")) {
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

    override fun onBackPressed() {
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            val intent = Intent().apply {
                putExtra("DELETE", temporaryStorageAdapter!!.storageData)
                putExtra("flag", 2)
            }
            setResult(RESULT_TEST, intent)
            finish()
            super.onBackPressed()
            return
        }
        Toast.makeText(this, "한번 더 클릭 시 홈으로 이동됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }

    private fun dataSet(): MutableList<TodoListData?> {
        tempStorage = arrayListOf(
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