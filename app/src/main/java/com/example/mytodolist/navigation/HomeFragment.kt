package com.example.mytodolist.navigation

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodolist.Adapter.TodoAdapter
import com.example.mytodolist.EditActivity
import com.example.mytodolist.databinding.FragmentHomeBinding
import com.example.mytodolist.model.TodoListData
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.mytodolist.R
import com.example.mytodolist.SwipeHelperCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var i = 4
    private lateinit var homeBinding: FragmentHomeBinding
    private var todoAdapter : TodoAdapter? = null
    private var dataPosition = 0 //수정시 데이터를 가져오기위한 인덱스
    private var checkBoxPosition = 0
    //모든 item
    private var data : MutableList<TodoListData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)

        initRecyclerView()
        initSwipeRefrech()

        //새로고침 클릭 시 애니메이션
        val reflashBtn : Button = homeBinding.reflashButton
        reflashBtn.setOnClickListener {

            //초기화
            data.clear()

            todoAdapter!!.listData = dataSet()

            homeBinding.recyclerView.startLayoutAnimation()
        }



        //type으로 추가인지 수정인지 받아오기
        homeBinding.fabAdd.setOnClickListener {
            val intent = Intent(activity, EditActivity::class.java).apply {
                putExtra("type","ADD")
            }
            requestActivity.launch(intent)

            todoAdapter!!.notifyDataSetChanged()
        }

        //체크 박스 클릭 시 adapter에 있는 paint값이 적용됨
        todoAdapter!!.setItemCheckBoxClickListener(object : TodoAdapter.ItemCheckBoxClickListener{
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todoListData = data[position]
                    checkBoxPosition = position
                    data[checkBoxPosition].isChecked = todoListData.isChecked

                    todoListData.isChecked = !todoListData.isChecked

                }
                todoAdapter!!.notifyDataSetChanged()
            }
        })


        //recyclerview의 아이템 클릭 시
        todoAdapter!!.setItemClickListener(object :TodoAdapter.ItemClickListener{
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todo = data[position]
                    dataPosition = position
                    val intent = Intent(activity, EditActivity::class.java).apply {
                        putExtra("type", "EDIT")
                        putExtra("item", todo)
                    }
                    requestActivity.launch(intent)
                }
            }
        })

        //리사이클러뷰에 스와이프 드래그달기
        val swipeHelperCallback = SwipeHelperCallback(todoAdapter!!).apply {
            //스와이프 후 고정위치 지정
            setFix(resources.displayMetrics.widthPixels.toFloat() / 4)
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(homeBinding.recyclerView)

        //다른 곳 터치 시 선택 뷰 닫기
        homeBinding.recyclerView.setOnTouchListener { _,_ ->
            swipeHelperCallback.removePreviousFix(homeBinding.recyclerView)
            false
        }


        return homeBinding.root
    }

    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            //getSerializableExtra = intent의 값을 보내고 받을때사용
            //타입 변경을 해주지 않으면 Serializable객체로 만들어지니 as로 캐스팅해주자
            val todo = it.data?.getSerializableExtra("todo") as TodoListData

            //flag가 -1에서
            when(it.data?.getIntExtra("flag", -1)) {
                0 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        //flag가 0이면 ADD이니 데이터의 값추가
                        data.add(todo)
                    }
                    Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        //flag가 1이면 EDIT이니 데이터의 값을 todo의 객체로 받은 값으로 데이터 수정
                        data[dataPosition] = todo
                    }
                    Toast.makeText(activity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            todoAdapter!!.notifyDataSetChanged()
        }
    }


    //나중에 데이터 받아오는 곳
    private fun dataSet(): MutableList<TodoListData> {
        data = mutableListOf(
            TodoListData(1,"test1",false),
            TodoListData(2,"test2",false),
            TodoListData(3,"test3",false),
            TodoListData(4,"test4",false),
            TodoListData(5,"test5",false),
            TodoListData(6,"test6",false),
            TodoListData(7,"test7",false),
            TodoListData(8,"test8",false)
        )
        return data
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initSwipeRefrech() {
        homeBinding.refreshSwipeLayout.setOnRefreshListener {
            //data.clear()
            todoAdapter!!.listData = data//dataSet()
            homeBinding.recyclerView.startLayoutAnimation()
            homeBinding.refreshSwipeLayout.isRefreshing = false

        }
        todoAdapter!!.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        todoAdapter = TodoAdapter()
        todoAdapter!!.listData = data
        homeBinding.recyclerView.adapter = todoAdapter
        homeBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}