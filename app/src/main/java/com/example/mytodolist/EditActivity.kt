package com.example.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.mytodolist.databinding.ActivityEditBinding
import com.example.mytodolist.model.TodoListData
import kotlinx.android.synthetic.main.activity_edit.view.*

class EditActivity : AppCompatActivity() {
    private lateinit var editBinding: ActivityEditBinding
    private var id = 0
    private var eTodo : TodoListData? = null

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
                    var todo = TodoListData(id, contentPost, false)
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
                    val todoData = TodoListData(eTodo!!.id, contentPost, eTodo!!.isChecked)
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
