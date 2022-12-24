package com.example.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodolist.Adapter.SearchItemAdapter
import com.example.mytodolist.Adapter.SearchWordAdapter
import com.example.mytodolist.Adapter.TodoAdapter
import com.example.mytodolist.databinding.ActivitySearchBinding
import com.example.mytodolist.model.SearchData
import com.example.mytodolist.model.SearchWordData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class SearchActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivitySearchBinding
    private var manager : LinearLayoutManager = LinearLayoutManager(this)
    private var searchWordAdapter : SearchWordAdapter? = null
    private var searchWordList = mutableListOf<SearchWordData?>()
    private val RESULT_SEARCH = 4
    var searchId = 0
    var sharedPref : SharedPref? = null
    private var setKey = "setting_search_history"
    private var isEnter = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        sharedPref = SharedPref(this)

        initWordRecyclerView()
        getHistory()
        //자동으로 키보드 띄우기
        searchViewkeyBoard(isEnter)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val close = mBinding.activitySearch.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        close.setOnClickListener {
            println("close")
        }
        val searchET = mBinding.activitySearch.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        searchET.setOnClickListener {
            println("et")
        }
        mBinding.activitySearch.queryHint = "검색어를 입력해주세요"
        mBinding.activitySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            //검색버튼 눌렀을 때 실행
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchWordList.add(SearchWordData(searchId, query))
                    //앱을 종료 후에도 기록이 남도록
                    sharedPref!!.setSearchHistory(this@SearchActivity, setKey, searchWordList)
                    searchWordAdapter!!.notifyDataSetChanged()
                    searchId += 1
                    isEnter = false
                    println(query)
                    println(searchWordList)
                    /*CoroutineScope(Dispatchers.IO).launch {
                        val intent = Intent(this@SearchActivity, SearchViewAcitivity::class.java).apply {
                            putExtra("searchword", query)
                        }
                        startActivity(intent)
                    }*/
                    val intent = Intent().apply {
                        putExtra("SEARCH", query)
                        putExtra("flag",4)
                    }
                    setResult(RESULT_SEARCH, intent)
                    finish()
                } else {

                }

                return true
            }
            //검색창에 값이 입력될 때 마다 실행
            override fun onQueryTextChange(newText: String?): Boolean {

                return true
            }
        })

        //recyclerview item클릭 시
        searchWordAdapter!!.setItemClickListener(object : SearchWordAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    println(searchWordList[position])
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                val intent = Intent().apply {

                }
                setResult(7, intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initWordRecyclerView() {
        searchWordAdapter = SearchWordAdapter()
        searchWordAdapter!!.searchWordData = searchWordList
        mBinding.searchWordRv.adapter = searchWordAdapter
        mBinding.searchWordRv.layoutManager = manager
        mBinding.searchWordRv.setHasFixedSize(true)

    }

    private fun getHistory() {
        var historyData = sharedPref!!.getSearchHistory(this@SearchActivity, setKey)
        if (historyData.isNotEmpty()) {
            searchWordList.clear()
            //searchWordList.addAll(historyData)
            for (i in historyData.indices) {
                searchWordList.add(SearchWordData(i,historyData[i]))
                println(historyData[i])
                println(searchWordList)
            }
        }
    }

    private fun searchViewkeyBoard(isEnter : Boolean) {
        mBinding.activitySearch.isIconified = !isEnter
    }
}