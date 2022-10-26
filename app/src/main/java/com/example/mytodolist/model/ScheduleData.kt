package com.example.mytodolist.model

data class ScheduleData(
    var id : Int, //position
    var scheduleText : String, //스케줄에 적은 내용
    var schedultTime : String
) : java.io.Serializable {

}