package com.example.mytodolist.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mytodolist.R
import com.example.mytodolist.TodoAdapter
import com.example.mytodolist.TodoListData
import com.example.mytodolist.databinding.FragmentHomeBinding

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


    private lateinit var homeBinding: FragmentHomeBinding
    private var todoAdapter : TodoAdapter? = null
    private val data : MutableList<TodoListData> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false)

        initRecyclerView()

        return homeBinding.root
    }

    private fun initRecyclerView() {
        data.add(TodoListData(1,"test"))
        data.add(TodoListData(2,"test"))
        data.add(TodoListData(3,"test"))
        data.add(TodoListData(4,"test"))
        data.add(TodoListData(5,"test"))
        data.add(TodoListData(6,"test"))
        data.add(TodoListData(7,"test"))
        data.add(TodoListData(8,"test"))
        data.add(TodoListData(9,"test"))
        data.add(TodoListData(10,"test"))
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