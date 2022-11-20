package com.example.mytodolist.model

data class TempListData(
    var id : Int, //position
    var content : String, //리스트에 적은 내용
    var isChecked : Boolean //checkbox 체크
) : java.io.Serializable {

}
