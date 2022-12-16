package com.example.mytodolist.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.TodoInterface
import com.example.mytodolist.databinding.RvLoadingBinding
import com.example.mytodolist.databinding.TodoItemBinding
import com.example.mytodolist.model.MyResponse
import com.example.mytodolist.model.TodoListData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class TodoAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    /*자바의 static을 없애고
        아래 동반 객체를 통해 정적 멤버를 정의하여
        메모리 활용을 효율적으로 합니다.*/
    companion object {
        private const val TAG_ITEM = 0
        private const val TAG_LOADING = 1
    }
    init {
        //아이템에 고유 식별자 (ID)를 부여하여
        // 이미 바인딩 된 적이 있는 아이템에 대해서는
        // 재 바인딩이 되지 않도록 한다.
        // (onBindViewHolder 호출 막음)
        setHasStableIds(true)
    }

    private lateinit var todoItemBinding: TodoItemBinding

    var listData = mutableListOf<TodoListData?>() //post , unfilter
    var temp : TodoListData? = null//넘겨주기용
    var testData : ArrayList<TodoListData?> = ArrayList<TodoListData?>()
    var isRemove = false //삭제 체크용
    private lateinit var context : Context
    private val checkBoxStatus = SparseBooleanArray()
    //검색
    var listFilter = ListFilter() //postfilter
    var filterContent = mutableListOf<TodoListData?>() //filterpost
    //데이터 연결
    val retrofit = Retrofit.Builder().baseUrl("https://waffle.gq")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service = retrofit.create(TodoInterface::class.java)
    var deleteServerData : String? = null
    var tempServerData = mutableListOf<TodoListData?>()


    inner class TodoViewHolder(var todoItemBinding: TodoItemBinding) : RecyclerView.ViewHolder(todoItemBinding.root) {
        private var position : Int? = null
        var checkselected : CheckBox = todoItemBinding.checkBox
        var todoT : TextView = todoItemBinding.todoText
        var tv_todoText : TextView = todoItemBinding.todoText

        fun bind(data : TodoListData, position: Int) {
            this.position = position
            //this.todoItemBinding.todoText.text = data.content
            //todoItemBinding.todoText.text = data.content
            //사용시 null이 잡히는 경우도 있어 조심하거나 다른거로 대체하기
            if (data.content != null) {
                todoItemBinding.todoText.text = data.content.toString()
            } else {
                todoItemBinding.todoText.text = ""
            }
            checkselected.isChecked = checkBoxStatus[adapterPosition]
            checkselected.isChecked = data.isChecked

            if (data.isChecked) {
                todoT.paintFlags = todoT.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                todoT.paintFlags = todoT.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }

            /*checkselected.setOnClickListener {
                if (!checkselected.isChecked) {
                    checkBoxStatus.put(adapterPosition,false)
                } else {
                    checkBoxStatus.put(adapterPosition,true)
                }
                notifyItemChanged(adapterPosition)
            }*/
            checkselected.setOnClickListener {
                itemCheckBoxClickListener.onClick(it,layoutPosition,listData[layoutPosition]!!.position)
            }

            todoItemBinding.root.setOnClickListener {
                itemClickListener.onClick(it,layoutPosition,listData[layoutPosition]!!.position)
            }

            todoItemBinding.removeIv.setOnClickListener {
                val builder : AlertDialog.Builder = AlertDialog.Builder(context)
                val ad : AlertDialog = builder.create()
                var deleteData = listData[this.layoutPosition]!!.content
                builder.setTitle(deleteData)
                builder.setMessage("정말로 삭제하시겠습니까?")

                builder.setNegativeButton("예",
                    DialogInterface.OnClickListener { dialog, which ->
                        ad.dismiss()
                        temp = listData[this.layoutPosition]!!
                        //extraditeData()
                        testData.add(temp)
                        deleteServerData = tempServerData[this.layoutPosition]!!.api_id
                        removeData(this.layoutPosition)
                        removeServerData(deleteServerData!!)
                        println(deleteServerData)
                    })

                builder.setPositiveButton("아니오",
                    DialogInterface.OnClickListener { dialog, which ->
                        ad.dismiss()
                    })
                builder.show()
            }
        }
    }



    inner class LoadingViewHolder(var loadingBinding: RvLoadingBinding) : RecyclerView.ViewHolder(loadingBinding.root) {
        val processBar : ProgressBar = loadingBinding.loadingPb
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var inflater : LayoutInflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        context = parent.context
        todoItemBinding = TodoItemBinding.inflate(inflater, parent, false)

        return if (viewType == TAG_ITEM) {
            TodoViewHolder(todoItemBinding)
        } else {
            val binding = RvLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LoadingViewHolder(binding)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoViewHolder) {
            //데이터 꼬임 문제를 해결가능
            //그러나 리사이클러뷰 사용하는 의미가 없음..
            //holder.setIsRecyclable(false)

            holder.bind(listData[holder.adapterPosition]!!, position)
            //val content : TodoListData = listData[holder.layoutPosition]!!
            //holder.tv_todoText.text = content.content
            //holder.bind(filterContent[position]!!, position)
        } else {

        }
        //holder.checkselected.isChecked

        /*holder.checkselected.setOnClickListener {
            if (!holder.checkselected.isChecked) {
                checkBoxStatus.put(position,false)
                todoItemBinding.todoText.paintFlags = todoItemBinding.todoText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            } else {
                checkBoxStatus.put(position,true)
                todoItemBinding.todoText.paintFlags = todoItemBinding.todoText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            notifyItemChanged(position)
        }*/
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (listData[position] != null) {
            TAG_ITEM
        } else {
            TAG_LOADING
        }
    }

    //filter
    override fun getFilter(): Filter {
        return listFilter
    }

    inner class ListFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {

            //val filterString = constraint.toString()
            //공벡제외 아무런 값도 입력하지 않았을 경우 ->원본배열
            val filteredList : ArrayList<TodoListData?> = ArrayList<TodoListData?>()

            val results = FilterResults()

            if (constraint.toString().lowercase(Locale.getDefault()).trim {it <= ' '}.isEmpty()) {
                //필터링 작업으로 계산된 모든 값
                results.values = listData
                //필터링 작업으로 계산된 값의 수
                results.count = listData.size

                //filteredList.addAll(listData)
                return results
            } else {
                val fs = constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }
                for (searchText in listData) {
                    if (searchText!!.content!!.lowercase(Locale.getDefault()).contains(fs)) {
                        filteredList.add(searchText)
                        println(filteredList)
                        println(filterContent)
                    }
                }
            }
            //filterlist를 results.values에 저장
            results.values = filteredList
            results.count = filteredList.size
            return results
        }

        //결과 필터 데이터 저장
        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            filterContent.clear()
            filterContent.addAll(results.values as Collection<TodoListData?>)
            notifyDataSetChanged()
        }
    }


    //데이터 Handle 함수
    fun removeData(position: Int) {
        listData.removeAt(position)
        temp = null
        notifyItemRemoved(position)
    }

    fun removeServerData(server_id : String) {
        service.deleteData(server_id).enqueue(object : Callback<MyResponse> {
            override fun onResponse(call: Call<MyResponse>, response: Response<MyResponse?>) {
                if (response.isSuccessful) {
                    //통신 성공
                    println("Success")
                } else {
                    //통신 실패
                    println("Fail")
                }
            }

            override fun onFailure(call: Call<MyResponse>, t: Throwable) {
                // 통신 실패 (인터넷 끊킴, 예외 발생 등 시스템적인 이유)
                println("Error" + t.message.toString())
            }
        })
    }


    fun swapData(beforePos : Int, afterPos : Int) {
        Collections.swap(listData, beforePos, afterPos)
        notifyItemMoved(beforePos,afterPos)
    }

    fun update(newList: MutableList<TodoListData?>) {
        this.listData = newList
        //this.listData = temp
        //notifyDataSetChanged()
    }

    interface ItemClickListener {
        fun onClick(view: View, position: Int, itemId: Int)
    }

    interface ItemCheckBoxClickListener {
        fun onClick(view: View, position: Int, itemId: Int)
    }

    private lateinit var itemClickListener: ItemClickListener
    private lateinit var itemCheckBoxClickListener: ItemCheckBoxClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun setItemCheckBoxClickListener(itemCheckBoxClickListener: ItemCheckBoxClickListener) {
        this.itemCheckBoxClickListener = itemCheckBoxClickListener
    }


}
