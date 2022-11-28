package com.example.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn : Button = findViewById(R.id.test_btn)
        loginBtn.setOnClickListener {
            if (user_email.text.toString().isEmpty()) {
                Toast.makeText(this,"Email을 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("email", user_email.text.toString())
                intent.putExtra("password", user_password.text.toString())
                startActivity(intent)
                finish()
            }
        }
    }
}