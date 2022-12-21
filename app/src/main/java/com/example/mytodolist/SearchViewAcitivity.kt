package com.example.mytodolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.mytodolist.databinding.ActivitySearchBinding
import com.example.mytodolist.databinding.ActivitySearchViewAcitivityBinding

class SearchViewAcitivity : AppCompatActivity() {
    private lateinit var searchViewAcitivityBinding: ActivitySearchViewAcitivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewAcitivityBinding = ActivitySearchViewAcitivityBinding.inflate(layoutInflater)
        setContentView(searchViewAcitivityBinding.root)


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
}