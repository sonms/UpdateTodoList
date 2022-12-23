package com.example.mytodolist.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.SharedPref
import com.example.mytodolist.databinding.RvLoadingBinding
import com.example.mytodolist.databinding.SearchItemLayoutBinding
import com.example.mytodolist.databinding.SearchWordLayoutBinding
import com.example.mytodolist.model.SearchData
import com.example.mytodolist.model.SearchWordData

class SearchWordAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        const val RV_TYPE_WORD = 0
        const val RV_TYPE_ITEM = 1
    }
    private lateinit var binding : SearchWordLayoutBinding
    var searchWordData = mutableListOf<SearchWordData?>()
    var searchItemData = mutableListOf<SearchData?>()
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

    inner class SearchItemViewHolder(private val binding : SearchItemLayoutBinding ) : RecyclerView.ViewHolder(binding.root) {
        private var position : Int? = null
        var searchItem_tv = binding.searchItemTv
        fun bind(search: SearchData, position : Int) {
            this.position = position
            searchItem_tv.text = search.searchItemText

            binding.root.setOnClickListener {
                itemClickListener.onClick(it,layoutPosition,searchWordData[layoutPosition]!!.id)
            }

            binding.searchRemoveIv.setOnClickListener {
                val builder : AlertDialog.Builder = AlertDialog.Builder(context)
                val ad : AlertDialog = builder.create()
                var deleteData = searchItemData[this.layoutPosition]!!.searchItemText
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        sharedPref = this.context?.let { SharedPref(it) }
        binding = SearchWordLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return if (viewType == RV_TYPE_WORD) {
            SearchWordViewHolder(binding)
        } else {
            val binding = SearchItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SearchItemViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SearchWordViewHolder) {
            holder.bind(searchWordData[position]!!, position)
            val content : SearchWordData = searchWordData[position]!!
            holder.searchWord_tv.text = content!!.searchWordText
        } else if (holder is SearchItemViewHolder){
            holder.bind(searchItemData[position]!!, position)
            val content : SearchData = searchItemData[position]!!
            holder.searchItem_tv.text = content!!.searchItemText
        }

    }

    override fun getItemCount(): Int {
        return searchWordData.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (searchWordData[position] != null) {
            RV_TYPE_WORD
        } else {
            RV_TYPE_ITEM
        }
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