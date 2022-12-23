package com.example.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodolist.Adapter.SearchItemAdapter
import com.example.mytodolist.Adapter.SearchWordAdapter
import com.example.mytodolist.databinding.ActivitySearchBinding
import com.example.mytodolist.databinding.ActivitySearchViewAcitivityBinding
import com.example.mytodolist.model.SearchData

class SearchViewAcitivity : AppCompatActivity() {
    private lateinit var searchViewAcitivityBinding: ActivitySearchViewAcitivityBinding
    private var manager : LinearLayoutManager = LinearLayoutManager(this)
    private var searchItemAdapter : SearchItemAdapter? = null
    private var searchItemList = mutableListOf<SearchData?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewAcitivityBinding = ActivitySearchViewAcitivityBinding.inflate(layoutInflater)
        setContentView(searchViewAcitivityBinding.root)

        val searchWord = intent.getStringExtra("searchword")

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


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

    private fun initItemRecyclerView() {
        searchItemAdapter = SearchItemAdapter()
        searchItemAdapter!!.searchItemData = searchItemList
        /*
        searchViewAcitivityBinding.searchWordRv.adapter = searchWordAdapter
        searchViewAcitivityBinding.searchWordRv.layoutManager = manager
        searchViewAcitivityBinding.searchWordRv.setHasFixedSize(true)
        */
    }
}