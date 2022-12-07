package com.example.mytodolist.model

import com.google.gson.annotations.SerializedName

class Paging (
    @SerializedName("total_pages")
    var total_pages : Int,

    @SerializedName("current_page")
    var current_page : Int,

    @SerializedName("is_last_page")
    var is_last_page : Boolean
): java.io.Serializable {


}
class TodoListResponseData (
    @SerializedName("todos")
    var todos : List<TodoListData>,

    @SerializedName("paging")
    var paging : Paging
): java.io.Serializable {


}
class MyResponse(
    @SerializedName("status")
    var status : Boolean,

    @SerializedName("data")
    val data : TodoListResponseData,

    val todoData : TodoListData
) : java.io.Serializable {


}
