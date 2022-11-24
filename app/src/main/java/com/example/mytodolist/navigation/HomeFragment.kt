package com.example.mytodolist.navigation

import android.animation.ObjectAnimator
import android.app.Activity.RESULT_OK
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytodolist.*
import com.example.mytodolist.Adapter.TodoAdapter
import com.example.mytodolist.databinding.FragmentHomeBinding
import com.example.mytodolist.model.TodoListData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val RESULT_TEST = 2

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var mainActivity: MainActivity
    private var todoAdapter : TodoAdapter? = null
    private var dataPosition = 0 //수정시 데이터를 가져오기위한 인덱스
    private var checkBoxPosition = 0
    private var manager : LinearLayoutManager = LinearLayoutManager(activity)
    //로딩 즉 item의 끝이며 스크롤의 끝인지
    private var isLoading = false
    //모든 item
    private var data : MutableList<TodoListData?> = mutableListOf()
    //삭제 후 임시저장 item 용
    var tempData : TodoListData? = null
    private var tempDataList : MutableList<TodoListData?> = mutableListOf()
    //검색용
    private var searchData : MutableList<TodoListData?> = mutableListOf()
    lateinit var filterString : ArrayList<String>
    private var searchView: SearchView? = null
    //mode 선택용
    private var isSelect : Boolean = true //true-light, false-dark
    private var mode : ImageSwitcher? = null
    private var switch : SwitchCompat? = null
    //상태유지
    var sharedPref : SharedPref? = null
    //fab 메뉴용
    private var isFabOpen = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)
        //tempData = data
        searchData.addAll(data)
        sharedPref = this.context?.let { SharedPref(it) }
        if (sharedPref!!.loadNightModeState()) {
            context?.setTheme(R.style.darktheme)
        } else {
            context?.setTheme(R.style.AppTheme)
        }
        //option = mainActivity.getSharedPreferences("option", MODE_PRIVATE)
        /*model.getAll().observe(this, Observer{
            noticeAdapter.setList(it.content)

            // 한 페이지당 게시물이 10개씩 들어있음.
            // 새로운 게시물이 추가되었다는 것을 알려줌 (추가된 부분만 새로고침)
            noticeAdapter.notifyItemRangeInserted((page - 1) * 10, 10)
        })*/
        initRecyclerView()
        initSwipeRefrech()
        initScrollListener()
        //툴바(search) 메뉴세팅
        setHasOptionsMenu(true)

        //fabmenu
        homeBinding.fabMenu.setOnClickListener {
            toggleFab()
        }

        //삭제 임시 저장 액티비티로 이동
        homeBinding.fabMode.setOnClickListener {
            val intent = Intent(activity, TemporaryStorageActivity::class.java).apply {
                putExtra("type", "delete")
                putExtra("item", todoAdapter!!.testData)
            }
            requestActivity.launch(intent)
        }

        //type으로 추가인지 수정인지 받아오기
        homeBinding.fabAdd.setOnClickListener {
            val intent = Intent(activity, EditActivity::class.java).apply {
                putExtra("type","ADD")
            }
            requestActivity.launch(intent)
            todoAdapter!!.notifyDataSetChanged()
        }

        //새로고침 클릭 시 애니메이션
        /*위로 새로고침하는 방식*/
        val reflashBtn : Button = homeBinding.reflashButton
        reflashBtn.setOnClickListener {

            //초기화
            data.clear()

            todoAdapter!!.listData = dataSet()

            todoAdapter!!.notifyDataSetChanged()

            homeBinding.recyclerView.startLayoutAnimation()
        }



        //체크박스 클릭 시
        todoAdapter!!.setItemCheckBoxClickListener(object : TodoAdapter.ItemCheckBoxClickListener{
            override fun onClick(view: View, position: Int, itemId: Int) {
                CoroutineScope(Dispatchers.IO).launch {
                    val todoListData = data[position]
                    checkBoxPosition = position
                    data[checkBoxPosition]!!.isChecked = todoListData!!.isChecked

                    todoListData.isChecked = !todoListData.isChecked
                }
                todoAdapter!!.notifyDataSetChanged()
            }
        })
        //recyclerview item클릭 시
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
        //다크모드 테스트
        /*val btn = homeBinding.test
        btn.setOnCheckedChangeListener {_, isChecked ->
            if (isChecked) {
                sharedPref!!.setNightModeState(true)
                restartApp()
            } else {
                sharedPref!!.setNightModeState(false)
                restartApp()
            }
        }*/

        return homeBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.toolbar_menu_item, menu)
        //val item = menu?.findItem(R.id.menu_action_search)


        var searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem : MenuItem = menu.findItem(R.id.menu_action_search)
        val selectMode : MenuItem = menu.findItem(R.id.select_mode)

        if (searchItem != null) {
            //val searchET = searchView!!.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
            //searchET.hint = "Search.."
            searchView = searchItem.actionView as SearchView
            searchView!!.queryHint = "Search.."
            searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                //검색버튼 입력시 호출, 근데 검색버튼이 없으므로 사용x
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //todoAdapter!!.filter.filter(query)
                    return false
                }

                //텍스트 입력/수정 시 호출
                override fun onQueryTextChange(s: String?): Boolean {
                    if (s != null) {
                        if (s.isNotEmpty()) {
                            todoAdapter!!.filter.filter(s)
                        //todoAdapter!!.filter.filter(s)
                        } else {
                            //todoAdapter!!.filterContent.addAll(tempData)
                            //todoAdapter!!.filter.filter(s)
                            println(todoAdapter!!.filterContent)
                        }
                    }
                    return false
                }
            })
        }


        //다크 모드 활성화 가능한 옵션메뉴
        switch = selectMode.actionView as SwitchCompat
        if (sharedPref!!.loadNightModeState()) {
            switch!!.isChecked = true
        }
        switch!!.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPref!!.setNightModeState(true)
                restartApp()
            } else {
                sharedPref!!.setNightModeState(false)
                restartApp()
            }
        }

        /*mode = selectMode.actionView as ImageSwitcher
        if (sharedPref!!.loadNightModeState()) {
            mode!!.isSelected = true
        }
        mode!!.setOnClickListener {
            if (isSelect) {
                isSelect = false
                mode!!.setImageResource(R.drawable.ic_baseline_light_mode_24)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                restartApp()
            } else {
                isSelect = true
                mode!!.setImageResource(R.drawable.ic_baseline_nightlight_round_24)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                restartApp()
            }
        }*/

        /*mode!!.setOnClickListener {
            if (isSelect) {
                isSelect = false
                mode!!.setImageResource(R.drawable.ic_baseline_nightlight_round_24)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                mode!!.setImageResource(R.drawable.ic_baseline_light_mode_24)
                isSelect = true
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }*/


        return super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select_mode -> {
                isSelect = !item.isChecked
                item.isChecked = isSelect
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

    //테마 변경 시 적용을 위한 재시작
    fun restartApp() {
        val intent = Intent(context?.applicationContext, MainActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            //getSerializableExtra = intent의 값을 보내고 받을때사용
            //타입 변경을 해주지 않으면 Serializable객체로 만들어지니 as로 캐스팅해주자
            val todo = it.data?.getSerializableExtra("todo") as TodoListData

            when(it.data?.getIntExtra("flag", -1)) {
                0 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        data.add(todo)
                    }
                    Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
                1 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        data[dataPosition] = todo
                    }
                    Toast.makeText(activity, "수정되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            todoAdapter!!.notifyDataSetChanged()
        } else if (it.resultCode == RESULT_TEST) {
            val delete = it.data?.getSerializableExtra("DELETE") as ArrayList<*>

            when(it.data?.getIntExtra("flag",-2)) {

            }
        }
    }


    //나중에 데이터 받아오는 곳
    private fun dataSet(): MutableList<TodoListData?> {
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

    private fun initSwipeRefrech() {
        homeBinding.refreshSwipeLayout.setOnRefreshListener {
            todoAdapter!!.listData = data//dataSet()
            homeBinding.recyclerView.startLayoutAnimation()
            homeBinding.refreshSwipeLayout.isRefreshing = false
        }
        todoAdapter!!.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        todoAdapter = TodoAdapter()
        todoAdapter!!.listData = data
        todoAdapter!!.filterContent = data
        homeBinding.recyclerView.adapter = todoAdapter
        manager.reverseLayout = true
        manager.stackFromEnd = true
        homeBinding.recyclerView.setHasFixedSize(true)
        homeBinding.recyclerView.layoutManager = manager
    }

    private fun initScrollListener(){
        homeBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutm = homeBinding.recyclerView.layoutManager as LinearLayoutManager
                //화면에 보이는 마지막 아이템의 position
                // 어댑터에 등록된 아이템의 총 개수 -1
                //데이터의 마지막이 아이템의 화면에 뿌려졌는지
                if (!isLoading) {
                    if (!homeBinding.recyclerView.canScrollVertically(1)) {
                        //가져온 data의 크기가 5와 같을 경우 실행
                        if (data.size == 5) {
                            isLoading = true
                            getMoreItem()
                        }
                    }
                }
            }
        })
    }

    //fab 메뉴 에니메이션
    private fun toggleFab() {
        if (isFabOpen) {
            ObjectAnimator.ofFloat(homeBinding.fabAdd, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(homeBinding.fabMode, "translationY", 0f).apply { start() }
            homeBinding.fabMenu.setImageResource(android.R.drawable.ic_input_add)
        } else {
            ObjectAnimator.ofFloat(homeBinding.fabAdd, "translationY", -200f).apply { start() }
            ObjectAnimator.ofFloat(homeBinding.fabMode, "translationY", -400f).apply { start() }
            homeBinding.fabMenu.setImageResource(android.R.drawable.ic_delete)
        }
        isFabOpen = !isFabOpen
    }

    private fun getMoreItem() {
        data.add(null)
        homeBinding.recyclerView.adapter!!.notifyItemInserted(data.size-1)
        /*data.removeAt(data.size-1)
        val currentSize = data.size
        for (i in currentSize + 1 until currentSize+5) {
            data.add(TodoListData(i, "test $i", false))
        }
        homeBinding.recyclerView.adapter!!.notifyDataSetChanged()
        isLoading = false*/
        /////////////////////
        /*val runnable = kotlinx.coroutines.Runnable {
            data.add(null) //null로 데이터의 끝을 알림
            todoAdapter!!.notifyItemInserted(data.size-1)
        }
        homeBinding.recyclerView.post(runnable)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            val runnable2 = Runnable{
                //null삭제 후
                data.removeAt(data.size-1)
                //스크롤끝이 어디인지
                val scrollPos = data.size
                todoAdapter!!.notifyItemRemoved(scrollPos)
                var currentSize = scrollPos //현재 data크기
                var next = currentSize + 5 //현재 data크기의 5추가만큼 다음데이터를 찾기

                //만약 현재사이즈가 data의 사이즈-5보다 작다면
                if (currentSize < data.size-10) {
                    while (currentSize-1 < next) {
                        //원래있던거 다시 넣어줌
                        data.add(tempData[currentSize])
                        currentSize++
                    }
                } else {
                    while (currentSize != tempData.size) {
                        data.add(tempData[currentSize])
                        currentSize++
                    }
                }

                if (data.get(dataPosition) == null) {
                    data.removeAt(dataPosition)
                }
                todoAdapter!!.update(data)
                isLoading = false
            }
            runnable2.run()
            println(data)
        }*/
        //성공//
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(java.lang.Runnable {
            data.removeAt(data.size - 1)
            val scrollPosition: Int = data.size
            todoAdapter!!.notifyItemRemoved(scrollPosition)
            var currentSize = scrollPosition
            //nextLimit과 +5값 조정이 페이지의 한계점
            val nextLimit = currentSize //현재는 1개추가
            while (currentSize - 1 < nextLimit) {
                data.add(TodoListData(currentSize, "Item $currentSize", false))
                currentSize++
            }
            todoAdapter!!.notifyDataSetChanged()
            isLoading = false
        }, 2000)
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