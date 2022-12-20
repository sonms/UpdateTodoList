package com.example.mytodolist.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.SharedPref
import com.example.mytodolist.databinding.SearchWordLayoutBinding
import com.example.mytodolist.model.SearchWordData

class SearchWordAdapter : RecyclerView.Adapter<SearchWordAdapter.SearchWordViewHolder>(){
    private lateinit var binding : SearchWordLayoutBinding

    var searchWordData = mutableListOf<SearchWordData?>()
    var sharedPref : SharedPref? = null
    private lateinit var context : Context
    private var setKey = "setting_search_history"

    inner class SearchWordViewHolder(private val binding : SearchWordLayoutBinding ) : RecyclerView.ViewHolder(binding.root) {
        private var position : Int? = null
        var searchWord_tv = binding.searchWordTv
        fun bind(searchHistory: SearchWordData, position : Int) {
            this.position = position
            searchWord_tv.text = searchHistory.searchWordText

            binding.root.setOnClickListener {
                itemClickListener.onClick(it,layoutPosition,searchWordData[layoutPosition]!!.id)
            }

            binding.searchwordRemoveIv.setOnClickListener {
                val builder : AlertDialog.Builder = AlertDialog.Builder(context)
                val ad : AlertDialog = builder.create()
                var deleteData = searchWordData[this.layoutPosition]!!.searchWordText
                builder.setTitle(deleteData)
                builder.setMessage("정말로 삭제하시겠습니까?")

                builder.setNegativeButton("예",
                    DialogInterface.OnClickListener { dialog, which ->
                        ad.dismiss()
                        removeData(this.layoutPosition)
                    })

                builder.setPositiveButton("아니오",
                    DialogInterface.OnClickListener { dialog, which ->
                        ad.dismiss()
                    })
                builder.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchWordViewHolder {
        context = parent.context
        sharedPref = this.context?.let { SharedPref(it) }
        binding = SearchWordLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchWordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchWordAdapter.SearchWordViewHolder, position: Int) {
        holder.bind(searchWordData[position]!!, position)
        val content : SearchWordData = searchWordData[position]!!
        holder.searchWord_tv.text = content!!.searchWordText
    }

    override fun getItemCount(): Int {
        return searchWordData.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    //데이터 Handle 함수
    fun removeData(position: Int) {
        searchWordData.removeAt(position)
        //삭제 구현 대신 데이터 덮어쓰기
        sharedPref!!.setSearchHistory(context, setKey, searchWordData)
        notifyItemRemoved(position)
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemId: Int)
    }
    private lateinit var itemClickListener: SearchWordAdapter.ItemClickListener

    fun setItemClickListener(itemClickListener: SearchWordAdapter.ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}