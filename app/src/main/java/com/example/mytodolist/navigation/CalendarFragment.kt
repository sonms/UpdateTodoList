package com.example.mytodolist.navigation

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.mytodolist.MainActivity
import com.example.mytodolist.R
import com.example.mytodolist.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalendarFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mainActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    //시작 달의 인스턴스(현재달)
    var startMonthCalendar = Calendar.getInstance()
    //마지막 달의 인스턴스
    var endMonthCalendar = Calendar.getInstance()

    //현재 년도
    val currentYear = startMonthCalendar.get(Calendar.YEAR)
    //현재 월
    val currentMonth = startMonthCalendar.get(Calendar.MONTH)
    //현재 날짜
    val currentDate = startMonthCalendar.get(Calendar.DATE)

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

        //마지막 달은 현재 달의 3을 더한 것 만큼 ex 1월->4월까지만 보여줌
        endMonthCalendar.set(Calendar.MONTH, currentMonth+3)

        //세팅 현재 10월 -> 내년 1월까지만 보여줌
       /*calendarBinding.calendarview.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
               //아래 코드를 그냥 맨끝 파라미터를 currentDate로 설정하면 그날로부터 전 날들은 전부 사라지기
               //때문에 그냥 1일로 설정
            .setMinimumDate(CalendarDay.from(currentYear, currentMonth, 1))
            .setMaximumDate(CalendarDay.from(currentYear, currentMonth+3, endMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()*/

        val startDate = CalendarDay.from(currentYear, currentMonth, currentDate)
        val endDate = CalendarDay.from(endMonthCalendar.get(Calendar.YEAR), endMonthCalendar.get(Calendar.MONTH), endMonthCalendar.get(Calendar.DATE))

        val sundayDeco = SundayDecorator()
        val saturdayDeco = SaturdayDecorator()
        val weekDayDeco = context?.let { WeekDayDecorator(it) }
        val monthDeco = MonthDecorator(startDate, endDate)
        val CurrentMonthDeco = CurrentMonthDecorator(startDate, endDate)

        calendarBinding.calendarview.addDecorators(sundayDeco, saturdayDeco, monthDeco, weekDayDeco, CurrentMonthDeco )

        return calendarBinding.root
    }

    inner class WeekDayDecorator(context: Context) : DayViewDecorator {

        private var date = CalendarDay.today()
        val drawble = context?.resources?.getDrawable(R.drawable.date_select_deco, null)

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return day?.equals(date)!!
        }

        override fun decorate(view: DayViewFacade?) {
            if (drawble != null) {
                view?.setBackgroundDrawable(drawble)
            }
        }

    }

    inner class SundayDecorator() : DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            //주어진 캘린더 인스턴스에 오늘의 정보를 복사
            day?.copyTo(calendar)
            //일주일을 받아옴
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            //그중 일요일을 리턴
            return weekDay == Calendar.SUNDAY
        }

        override fun decorate(view: DayViewFacade?) {
            //하루(일요일)의 전체 텍스트에 범위의 색 추가
            view?.addSpan(object :ForegroundColorSpan(Color.RED){})
        }

    }

    inner class SaturdayDecorator() : DayViewDecorator {
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            //주어진 캘린더 인스턴스에 오늘의 정보를 복사
            day?.copyTo(calendar)
            //토요일을 받아옴
            val weekDay = calendar.get(Calendar.DAY_OF_WEEK)
            //그중 토요일을 리턴
            return weekDay == Calendar.SATURDAY
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object :ForegroundColorSpan(Color.BLUE){})
        }

    }

    //현재 한 달의 1~30(31)을 제외한 이전 달 또는 다음 달의 날 체크
    inner class MonthDecorator(s : CalendarDay, e : CalendarDay) :DayViewDecorator {
        private val startDate = s
        private val endDate = e
        //date : 구체적 특정 날짜, day : 하루
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return (day?.month == startDate.month && day.day > startDate.day)
                    || (day?.month == endDate.month && day.day < endDate.day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : ForegroundColorSpan(Color.GRAY){})
            //선택 불가 true
            view?.setDaysDisabled(true)
        }
    }

    inner class CurrentMonthDecorator(s : CalendarDay, e : CalendarDay) : DayViewDecorator{
        val startDate = s
        val endDate = e
        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return (day?.month == startDate.month && day.day <= startDate.day)
                    || (day?.month == endDate.month && day.day >= endDate.day)
                    || (endDate.month < day?.month!! && day.month < startDate.month)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(object : StyleSpan(Typeface.BOLD){})
            view?.addSpan(object :RelativeSizeSpan(1.4f){})
        }
    }

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