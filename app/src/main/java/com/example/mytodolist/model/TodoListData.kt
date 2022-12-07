package com.example.mytodolist.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TodoListData(
    @SerializedName("id")
    var api_id : String,

    var position: Int, //position

    @SerializedName("content")
    var content: String?, //리스트에 적은 내용

    @SerializedName("completed")
    var isChecked: Boolean //checkbox 체크

) : java.io.Serializable {

}
