package com.example.mytodolist

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TimePicker
import android.widget.TimePicker.OnTimeChangedListener
import androidx.appcompat.app.AppCompatActivity
import com.example.mytodolist.databinding.ActivityEditBinding
import com.example.mytodolist.databinding.ActivityScheduleEditBinding
import com.example.mytodolist.model.ScheduleData
import com.example.mytodolist.model.TodoListData
import kotlinx.android.synthetic.main.activity_schedule_edit.*
import java.text.DateFormat
import java.util.Calendar

class ScheduleEditActivity : AppCompatActivity() {

    private lateinit var scheduleEditBinding: ActivityScheduleEditBinding
    private lateinit var timepicker : TimePicker
    private var targetTime : String = ""
    private var targetDay : String = ""
    private var id = 0
    private var scheduleContent : ScheduleData? = null
    private lateinit var setAlarmTime : Calendar

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleEditBinding = ActivityScheduleEditBinding.inflate(layoutInflater)
        setContentView(scheduleEditBinding.root)

        timepicker = scheduleEditBinding.timePicker

        val type = intent.getStringExtra("type") //schedule
        targetDay = intent.getStringExtra("time").toString()

        scheduleEditBinding.selectTime.text = targetDay

        timepicker.setOnTimeChangedListener(OnTimeChangedListener { view, hourOfDay, minute ->
            var hour = hourOfDay
            setAlarmTime = Calendar.getInstance()
            //오전 오후를 표시하기 위해 사용된 코드
            if (hour > 12) {
                hour -= 12
                scheduleEditBinding.tvTime.text = "오후 $hour 시 $minute 분"
                targetTime = "오후 $hour 시 $minute 분"

                //시간 설정
                setAlarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                setAlarmTime.set(Calendar.MINUTE, minute)
                setAlarmTime.set(Calendar.SECOND, 0)
            } else {
                scheduleEditBinding.tvTime.text = "오전 $hour 시 $minute 분"
                targetTime = "오전 $hour 시 $minute 분"
            }
        })

        scheduleEditBinding.scheduleSaveBtn.setOnClickListener {
            val contentPost = scheduleEditBinding.scheduleText.text.toString() //schedule edit text

            if (type.equals("schedule")) {
                if (contentPost.isNotEmpty()) {
                    //객체생성은 따로 입력받아서 하는거
                    var schedule = ScheduleData(id, contentPost, targetTime)
                    startAlarm(setAlarmTime, contentPost)
                    println(setAlarmTime.time)
                    val intent = Intent().apply {
                        putExtra("schedule", schedule)
                        putExtra("flag",3)
                    }
                    id += 1
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }
        }
    }

    //알림 설정
    private fun startAlarm(c : Calendar, content : String?) {
        var alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)

        var bundle = Bundle()
        bundle.putString("time", curTime)
        bundle.putString("content", content)

        var intent = Intent(this, AlertReceiver::class.java).apply {
            putExtra("bundle",bundle)
        }

        //intent를 당장 수행하지 않고 특정시점에 수행하도록 미룰 수 있는 intent
        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0).apply {

        }

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1)
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, setAlarmTime.timeInMillis, pendingIntent)

    }

    //알림 취소
    private fun cancelAlarm() {
        var alarmManager : AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        var intent = Intent(this, AlertReceiver::class.java)

        //intent를 당장 수행하지 않고 특정시점에 수행하도록 미룰 수 있는 intent
        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0)

        alarmManager.cancel(pendingIntent)
    }
}