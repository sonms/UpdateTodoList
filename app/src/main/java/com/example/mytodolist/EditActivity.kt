package com.example.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.mytodolist.databinding.ActivityEditBinding
import com.example.mytodolist.model.MyResponse
import com.example.mytodolist.model.TodoListData
import kotlinx.android.synthetic.main.activity_edit.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditActivity : AppCompatActivity() {
    private lateinit var editBinding: ActivityEditBinding
    private var id = 0
    private var eTodo : TodoListData? = null

    //
    val retrofit = Retrofit.Builder().baseUrl("https://waffle.gq")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(TodoInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        editBinding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(editBinding.root)

        val type = intent.getStringExtra("type")

        if (type.equals("ADD")) {
            editBinding.textBtn.text = "추가하기"
        } else if (type.equals("EDIT")){
            eTodo = intent.getSerializableExtra("item") as TodoListData?
            editBinding.editText.setText(eTodo!!.content)
            editBinding.textBtn.text = "수정하기"
        }

        editBinding.textBtn.setOnClickListener {
            val contentPost = editBinding.editText.text.toString()

            if (type.equals("ADD")) {
                if (contentPost.isNotEmpty()) {
                    //객체생성은 따로 입력받아서 하는거

                    var todo = TodoListData("Api_id",id, contentPost, false)

                    service.addData(todo).enqueue(object : Callback<MyResponse> {
                        override fun onResponse(
                            call: Call<MyResponse>,
                            response: Response<MyResponse?>
                        ) {
                            if (response.isSuccessful) {
                                //Toast추가 성공메세지
                            } else {
                                println("fail")
                            }
                        }
                        override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                            println("실패")
                        }
                    })

                    val intent = Intent().apply {
                        putExtra("todo", todo)
                        putExtra("flag",0)
                    }
                    id += 1
                    setResult(RESULT_OK, intent)
                    finish()
                }
            } else if(type.equals("EDIT")){
                if (contentPost.isNotEmpty()) {
                    val todoData = TodoListData("Api_id",eTodo!!.position, contentPost, eTodo!!.isChecked)

                    val intent = Intent().apply {
                        putExtra("todo", todoData)
                        putExtra("flag", 1)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }
}
