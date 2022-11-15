package com.example.mytodolist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.Adapter.TodoAdapter
import com.example.mytodolist.databinding.ActivityMainBinding
import com.example.mytodolist.navigation.AccountFragment
import com.example.mytodolist.navigation.CalendarFragment
import com.example.mytodolist.navigation.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityMainBinding
    private lateinit var main_content : LinearLayout //xml의 content를 담는 layout
    private lateinit var bottom_navigationview : BottomNavigationView
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar
    /*private var homeFragment : HomeFragment? = null
    private var blankFragment: BlankFragment? = null
    private var accountFragment: AccountFragment? = null*/
    private val TAG_HOME = "home_fragment"
    private val TAG_CALENDAR = "calendar_fragment"
    private val TAG_ACCOUNT = "account_fragment"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mBinding.root
        setContentView(view)
        setFragment(TAG_HOME, HomeFragment())

        initBottomNavigationBar()
    }

    private fun initBottomNavigationBar() {
        main_content = findViewById(R.id.main_content)
        bottom_navigationview = findViewById(R.id.bottomNavigationView)

        mBinding.bottomNavigationView.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.home -> {

                    var manager = findViewById<RecyclerView>(R.id.recyclerView).layoutManager as? LinearLayoutManager
                    //smooth하게 올리는 방식 빠르게올리는 방식은 아래 scrollToLastItem
                    val smoothScroller = object : LinearSmoothScroller(this) {
                        override fun getVerticalSnapPreference(): Int {
                            return LinearSmoothScroller.SNAP_TO_END
                        }
                    }
                    val last = manager!!.findLastVisibleItemPosition()
                    smoothScroller.targetPosition = last
                    if (last >= 0) {
                        manager!!.startSmoothScroll(smoothScroller)
                    } else {

                    }
                    setFragment(TAG_HOME, HomeFragment())

                }
                R.id.calendar -> setFragment(TAG_CALENDAR, CalendarFragment())
                R.id.account -> setFragment(TAG_ACCOUNT, AccountFragment())
            }
            true
        }

        /*bottom_navigationview.run {
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.home -> {
                        if (homeFragment == null) {
                            homeFragment = HomeFragment()
                            changeFragment(HomeFragment())
                        }
                        if (homeFragment != null) {
                            supportFragmentManager.beginTransaction().show(homeFragment!!).commit()
                        }
                        if (blankFragment != null) {
                            supportFragmentManager.beginTransaction().hide(blankFragment!!).commit()
                        }
                        if (accountFragment != null) {
                            supportFragmentManager.beginTransaction().hide(accountFragment!!).commit()
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.contemplating -> {
                        if (blankFragment == null) {
                            blankFragment = BlankFragment()
                            changeFragment(BlankFragment())
                        }
                        if (homeFragment != null) {
                            supportFragmentManager.beginTransaction().hide(homeFragment!!).commit()
                        }
                        if (blankFragment != null) {
                            supportFragmentManager.beginTransaction().show(blankFragment!!).commit()
                        }
                        if (accountFragment != null) {
                            supportFragmentManager.beginTransaction().hide(accountFragment!!).commit()
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.account -> {
                        if (accountFragment == null) {
                            accountFragment = AccountFragment()
                            changeFragment(AccountFragment())
                        }
                        if (homeFragment != null) {
                            supportFragmentManager.beginTransaction().hide(homeFragment!!).commit()
                        }
                        if (blankFragment != null) {
                            supportFragmentManager.beginTransaction().hide(blankFragment!!).commit()
                        }
                        if (accountFragment != null) {
                            supportFragmentManager.beginTransaction().show(accountFragment!!).commit()
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                }
                false
            }
            //원래 처음으로 설정되는 코드로 사용됬던거
            //selectedItemId = R.id.home
        }*/
    }

    fun setFragment(tag : String, fragment: Fragment) {
        val manager : FragmentManager = supportFragmentManager
        val bt = manager.beginTransaction()

        if (manager.findFragmentByTag(tag) == null) {
            bt.add(R.id.main_content, fragment, tag)
        }

        val home = manager.findFragmentByTag(TAG_HOME)
        val calendar = manager.findFragmentByTag(TAG_CALENDAR)
        val account = manager.findFragmentByTag(TAG_ACCOUNT)

        if (home != null) {
            bt.hide(home)
        }
        if (calendar != null) {
            bt.hide(calendar)
        }
        if (account != null) {
            bt.hide(account)
        }

        if (tag == TAG_HOME) {
            if (home != null) {
                bt.show(home)
            }
        }
        else if (tag == TAG_CALENDAR) {
            if (calendar != null) {
                bt.show(calendar)
            }
        }
        else if (tag == TAG_ACCOUNT) {
            if (account != null) {
                bt.show(account)
            }
        }
        bt.commitAllowingStateLoss()
    }
    private fun scrollToLastItem(view: View) {
        var manager = findViewById<RecyclerView>(R.id.recyclerView).layoutManager as? LinearLayoutManager
        val last = manager!!.findLastVisibleItemPosition()
        Handler(Looper.getMainLooper()).postDelayed(
            Runnable { /*manager!!.scrollToPositionWithOffset(last,0)*/ },
            200
        )
    }

    fun changeFragment(fragment : Fragment) {
        //프래그먼트를 교체 하는 작업을 수행할 수 있게 해줍니다.
        supportFragmentManager
            .beginTransaction()
            .add(R.id.main_content, fragment)
            .commit()
    }
}