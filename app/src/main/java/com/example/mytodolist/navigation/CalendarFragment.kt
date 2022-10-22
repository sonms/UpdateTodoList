package com.example.mytodolist.navigation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.mytodolist.databinding.FragmentCalendarBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var calendarBinding: FragmentCalendarBinding
    /*private lateinit var selectedDate : LocalDate
    private lateinit var calendarAdapter : CalendarAdapter
    private var monthDate : ArrayList<String> = ArrayList()*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarBinding = FragmentCalendarBinding.inflate(inflater, container, false)
        return calendarBinding.root
    }
    /*커스텀 캘린더뷰 실패작..
    private fun setMonthView() {
        //텍스트뷰 셋팅
        calendarBinding.yearMonthText.text = dateOfYearMonth(selectedDate)

        monthDate = dateSetting(selectedDate)

        calendarAdapter = CalendarAdapter(monthDate)

        calendarBinding.calendarRecyclerview.adapter = calendarAdapter
        //grid 형식으로 7개 설정(한 주)
        calendarBinding.calendarRecyclerview.layoutManager = GridLayoutManager(activity, 7)


    }

    private fun dateSetting(date : LocalDate) : ArrayList<String> {
        val dateSet : ArrayList<String> = ArrayList()
        val y_month : YearMonth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            YearMonth.from(date)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val lastDate = y_month.lengthOfMonth() //월의 마지막날짜
        val firstDate = selectedDate.withDayOfMonth(1) //월의 첫날짜
        val fotw : Int = firstDate.dayOfWeek.value//first_day_of_the_week

        for (i in 1..41) {
            //만약 한 주의 첫 요일이 i보다 작거나 같음 또는 i가 마지막요일 + 첫요일보다 크다면
            if (i <= fotw || i > lastDate + fotw) {
                dateSet.add("")
            } else {
                dateSet.add((i-fotw).toString())
            }
        }
        return monthDate
    }

    //날짜 타입 설정
    private fun dateOfYearMonth(date: LocalDate): String{

        var formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("yyyy MM월")
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        // 받아온 날짜를 해당 포맷으로 변경
        return date.format(formatter)
    }*/

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}