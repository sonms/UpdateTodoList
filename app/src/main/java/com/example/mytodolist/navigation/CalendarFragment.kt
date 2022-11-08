package com.example.mytodolist.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.mytodolist.Adapter.ScheduleAdapter
import com.example.mytodolist.Adapter.TodoAdapter
import com.example.mytodolist.EditActivity
import com.example.mytodolist.MainActivity
import com.example.mytodolist.ScheduleEditActivity
import com.example.mytodolist.databinding.FragmentCalendarBinding
import com.example.mytodolist.model.ScheduleData
import com.example.mytodolist.model.TodoListData
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.android.synthetic.main.fragment_calendar.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_schedule.*
import kotlinx.android.synthetic.main.item_schedule.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Month
import java.time.Year
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CalendarFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mainActivity: MainActivity
    //context받기
    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    private var scheduleAdapter : ScheduleAdapter? = null

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

    private var calendarList : ArrayList<CalendarDay> = ArrayList()
    private lateinit var calendarBinding: FragmentCalendarBinding
    /*private lateinit var selectedDate : LocalDate
    private lateinit var calendarAdapter : CalendarAdapter
    private var monthDate : ArrayList<String> = ArrayList()*/
    private var addScheduleData : MutableList<ScheduleData?> = mutableListOf()
    private var targetDay : String = ""
    private var dot_y : Int = 0
    private var dot_m : Int = 0
    private var dot_d : Int = 0
    private var date = CalendarDay.today()

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

        //마지막 달은 현재 달의 3을 더한 것 만큼 ex 1월->6?5?월까지만 보여줌
        endMonthCalendar.set(Calendar.MONTH, currentMonth+5)
        addScheduleData.add(ScheduleData(0, "test", "10시"))
        addScheduleData.add(ScheduleData(1, "test1", "50시"))
        addScheduleData.add(ScheduleData(2, "test2", "30시"))
        addScheduleData.add(ScheduleData(3, "test3", "20시"))


        initViewPager()
        //세팅 현재 10월 -> 3?월까지만 보여줌
       /*calendarBinding.calendarview.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
               //아래 코드를 그냥 맨끝 파라미터를 currentDate로 설정하면 그날로부터 전 날들은 전부 사라지기
               //때문에 그냥 1일로 설정
            .setMinimumDate(CalendarDay.from(currentYear, currentMonth, 1))
            .setMaximumDate(CalendarDay.from(currentYear, currentMonth+5, endMonthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()*/
        calendarBinding.calendarview.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            //test용 변수 .. tartgetDay는 나중에 toast나 다른 기능에 사용예정
            dot_y = date.year
            dot_m = date.month + 1
            dot_d = date.day
            targetDay = "$dot_y.$dot_m.$dot_d"
            /*var date = Calendar.getInstance()
            date.set(dot_y, dot_m, dot_d)

            // 달력에 표시할 날짜 day List에 넣기
            var day = CalendarDay.from(date) // Calendar 자료형을 넣어주면 됨
            calendarList.add(day)*/

            //val eventDeco = EventDecorator(calendarList, mainActivity, Color.BLUE)
            calendarBinding.calendarview.addDecorator(EventDecorator(Collections.singleton(date), Color.BLUE)) //선택시찍힘
            //calendarBinding.calendarview.addDecorator(EventDecorator(calendarList, mainActivity, Color.BLUE))
            //dotDecorator(calendarBinding.calendarview)
            //선택초기화화
            calendarBinding.calendarview.clearSelection()
            val intent = Intent(activity, ScheduleEditActivity::class.java).apply {
                putExtra("type","schedule")
                putExtra("time", targetDay)
            }
            requestActivity.launch(intent)
        })

        val startDate = CalendarDay.from(currentYear, currentMonth, currentDate)
        val endDate = CalendarDay.from(endMonthCalendar.get(Calendar.YEAR), endMonthCalendar.get(Calendar.MONTH), endMonthCalendar.get(Calendar.DATE))

        val sundayDeco = SundayDecorator()
        val saturdayDeco = SaturdayDecorator()
        val toDayDeco = context?.let { ToDayDecorator(it) }
        //val monthDeco = MonthDecorator(startDate, endDate)
        //val CurrentMonthDeco = CurrentMonthDecorator(startDate, endDate)
        //calendarList.add(CalendarDay.today())
        //calendarList.add(CalendarDay.from(2022, 10, 25))
        val eventDeco = EventDecorator(calendarList, Color.BLUE)
        calendarBinding.calendarview.addDecorators(sundayDeco, saturdayDeco, /*monthDeco,*/ toDayDeco/*, CurrentMonthDeco*/ /*, eventDeco*/)

        //dotDecorator(calendarBinding.calendarview)

        return calendarBinding.root
    }

    private fun initViewPager() {
        scheduleAdapter = ScheduleAdapter()
        scheduleAdapter!!.scheduleData = addScheduleData
        calendarBinding.scheduleViewpager2.adapter = scheduleAdapter
        //calendarBinding.scheduleViewpager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }


    private val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            //getSerializableExtra = intent의 값을 보내고 받을때사용
            //타입 변경을 해주지 않으면 Serializable객체로 만들어지니 as로 캐스팅해주자
            val schedule = it.data?.getSerializableExtra("schedule") as ScheduleData

            when(it.data?.getIntExtra("flag", -1)) {
                /*0 -> {
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
            }*/
                //3 = calendarview에서의 Flag
                3 -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        addScheduleData.add(schedule)
                    }
                    calendarBinding.setDate.text = date.toString()
                    Toast.makeText(activity, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /*fun dotDecorator(calendarview : MaterialCalendarView?) {
        var dates = kotlin.collections.ArrayList<CalendarDay>()
        CoroutineScope(Dispatchers.IO).launch {
            if (calendarList.size != 0) {
                for (i in 0 until calendarList.size) {
                    var dy = calendarList[i].year
                    var dm = calendarList[i].month
                    var dd = calendarList[i].day
                    var date = Calendar.getInstance()
                    date.set(dy, dm, dd)
                    var day = CalendarDay.from(date)
                    dates.add(day)
                    println(dates)
                }
            }
        }
        Handler(Looper.getMainLooper()).postDelayed({
            calendarview!!.removeDecorators()
            calendarview!!.invalidateDecorators()
            calendarview.addDecorators(SundayDecorator(), SaturdayDecorator(), ToDayDecorator(mainActivity))
            if (dates.size > 0) {
                calendarview!!.addDecorator(EventDecorator(dates, Color.BLUE))
            }
            println(dates.size)
        }, 0)
    }*/

    //오늘을 색다르게 데코
    inner class ToDayDecorator(context: Context) : DayViewDecorator {

        private var date = CalendarDay.today()
        val drawble = context?.resources?.getDrawable(com.example.mytodolist.R.drawable.date_select_deco, null)

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

    inner class EventDecorator(dates: Collection<CalendarDay>?,
                                //context: Activity,
                               color : Int
                               /*textView: TextView?*/) : DayViewDecorator {
        private var dates : HashSet<CalendarDay>
        //private var textView : TextView
        //private var drawable : Drawable
        private var color = 0

        init {
            //drawable = context.resources.getDrawable(com.example.mytodolist.R.drawable.test, null)
            this.dates = dates?.let { HashSet(it) }!!
            this.color = color
        }

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade?) {
            //view!!.setSelectionDrawable(drawable)
            view!!.addSpan(DotSpan(5F, color))
        }
    }

    //현재 한 달의 1~30(31)을 제외한 이전 달 또는 다음 달의 날 체크
    /*inner class MonthDecorator(s : CalendarDay, e : CalendarDay) :DayViewDecorator {
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
    }*/

    /*inner class CurrentMonthDecorator(s : CalendarDay, e : CalendarDay) : DayViewDecorator{
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