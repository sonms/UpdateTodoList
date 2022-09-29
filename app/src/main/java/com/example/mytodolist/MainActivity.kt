package com.example.mytodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.mytodolist.navigation.AccountFragment
import com.example.mytodolist.navigation.BlankFragment
import com.example.mytodolist.navigation.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var main_content : LinearLayout //xml의 content를 담는 layout
    private lateinit var bottom_navigationview : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigationBar()
    }

    private fun initBottomNavigationBar() {
        main_content = findViewById(R.id.main_content)
        bottom_navigationview = findViewById(R.id.bottomNavigationView)

        bottom_navigationview.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.home -> {
                        changeFragment(HomeFragment())
                    }
                    R.id.contemplating -> {
                        changeFragment(BlankFragment())
                    }
                    R.id.account -> {
                        changeFragment(AccountFragment())
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }
    fun changeFragment(fragment : Fragment) {
        //프래그먼트를 교체 하는 작업을 수행할 수 있게 해줍니다.
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_content, fragment) //activity_test의 main content에 replace
            .commit()
    }
}