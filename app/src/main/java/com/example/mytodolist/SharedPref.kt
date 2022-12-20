package com.example.mytodolist

import android.content.Context
import android.content.SharedPreferences
import com.example.mytodolist.model.SearchWordData
import org.json.JSONArray
import org.json.JSONException


class SharedPref(context: Context) {
    var mySharedPref: SharedPreferences
    var storeSharedPref : SharedPreferences
    init {
        mySharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE)
        storeSharedPref = context.getSharedPreferences("store_data", Context.MODE_PRIVATE)
    }

    fun setNightModeState(state: Boolean?) {
        val editor = mySharedPref.edit()
        editor.putBoolean("NightMode", state!!)
        editor.apply()
    }

    fun loadNightModeState(): Boolean {
        return mySharedPref.getBoolean("NightMode", false)
    }

    fun setSearchHistory(context: Context, key: String, values: MutableList<SearchWordData?>) {
        //val prefs: SharedPreferences = storeSharedPref
        val editor = storeSharedPref.edit()
        val data : JSONArray = JSONArray()

        for (i in 0 until values.size) {
            data.put(values[i]!!.searchWordText)
        }
        if (values.isNotEmpty()) {
            editor.putString(key, data.toString())
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    fun getSearchHistory(context: Context, key: String) : MutableList<String> {
        //val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val json = storeSharedPref.getString(key, null)
        val historyArr : ArrayList<String> = ArrayList()
        if (json != null) {
            try {
                val data : JSONArray = JSONArray(json)
                for (i in 0 until data.length()) {
                    val s = data.optString(i)
                    historyArr.add(s)
                }
            } catch (e : JSONException) {
                e.printStackTrace()
            }
        }

        return historyArr
    }
}