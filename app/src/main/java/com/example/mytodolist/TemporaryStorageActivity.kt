package com.example.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodolist.Adapter.TemporaryStorageAdapter
import com.example.mytodolist.databinding.ActivityTemporaryStorageBinding
import com.example.mytodolist.model.MyResponse
import com.example.mytodolist.model.TodoListData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class TemporaryStorageActivity : AppCompatActivity() {
    private val RESULT_TEST = 2
    private lateinit var temporaryStorageBinding: ActivityTemporaryStorageBinding
    private var temporaryStorageAdapter : TemporaryStorageAdapter? = null
    private var tempStorage : MutableList<TodoListData?> = mutableListOf()//ArrayList<TodoListData?>() //recyclerview 데이터
    private lateinit var trashTempDataList : List<TodoListData>
    private var manager : LinearLayoutManager = LinearLayoutManager(this)
    private var tempItem : MutableList<TodoListData?> = mutableListOf() //: ArrayList<TodoListData?> = ArrayList() //받기
    //뒤로가기 이벤트
    var backPressedTime : Long = 0

    //데이터
    val retrofit = Retrofit.Builder().baseUrl("https://waffle.gq")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(TodoInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        temporaryStorageBinding = ActivityTemporaryStorageBinding.inflate(layoutInflater)
        setContentView(temporaryStorageBinding.root)


        trashDataSet()
        initStorageRecyclerView()

        //뒤로 가기 버튼(setDisplayHomeAsUpEnabled)을 만드는 코드
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val type = intent.getStringExtra("type")
        //받는거 하나 보여주는 거 하나 따로..?->ArrayList 로 해결
        //->어차피 서버에서 받아올 거니 삭제
        /*if (type.equals("delete")) {
            tempItem = intent.getSerializableExtra("item") as ArrayList<TodoListData?>
            //tempStorage.addAll(tempItem)
        }*/



        temporaryStorageBinding.testData.setOnClickListener {
            tempStorage.forEach { i->
                println("temtep"+"$i")
            }
            println("s" + tempStorage.size)
            println("sl" + trashTempDataList.size)
            println("?" + temporaryStorageAdapter!!.storageDataList.size)
            //temporaryStorageAdapter!!.storageData = dataSet()
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                val intent = Intent().apply {
                    putExtra("DELETE", temporaryStorageAdapter!!.storageDataList as ArrayList<TodoListData?>)
                    putExtra("flag", 2)
                }
                setResult(RESULT_TEST, intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        //2.5초이내에 한 번 더 뒤로가기 클릭 시
        if (System.currentTimeMillis() - backPressedTime < 2500) {
            val intent = Intent().apply {
                putExtra("DELETE", temporaryStorageAdapter!!.storageDataList as ArrayList<TodoListData?>)
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

    private fun trashDataSet() {
        service.getTrashDataByPage(0, 10).enqueue(object : Callback<MyResponse> {
            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse?>) {
                if (response.isSuccessful) {
                    //데이터 불러오기가 완료되면
                    tempStorage.clear()
                    //통신 성공
                    trashTempDataList = response.body()!!.data.todos

                    trashTempDataList.forEach {
                        tempStorage.add(it)
                    }
                    temporaryStorageAdapter!!.notifyDataSetChanged()

                    /*trashTempDataList.forEach {
                        //data.add(it)
                        //searchData.add(it)
                        tempStorage.add(it)
                    }*/

                    /*CoroutineScope(Dispatchers.IO).launch {
                        for (list in trashTempDataList) {
                            tempStorage.add(list)
                        }
                    }*/



                    /*CoroutineScope(Dispatchers.Main).launch {
                        if (tempStorage.size == 0) {
                            temporaryStorageBinding.nullTv.visibility = View.VISIBLE
                        } else {
                            //println(temporaryStorageAdapter!!.storageDataList.size)
                            temporaryStorageBinding.nullTv.visibility = View.GONE
                        }
                    }*/

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

    private fun initStorageRecyclerView() {
        temporaryStorageAdapter = TemporaryStorageAdapter()
        //tempStorage = temporaryStorageAdapter!!.storageData
        temporaryStorageAdapter!!.storageDataList = tempStorage
        temporaryStorageAdapter!!.trashDataList = tempStorage
        temporaryStorageBinding.temporaryStorageRecyclerview.adapter = temporaryStorageAdapter
        temporaryStorageBinding.temporaryStorageRecyclerview.layoutManager = manager
    }

}