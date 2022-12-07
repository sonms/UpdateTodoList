package com.example.mytodolist

import com.example.mytodolist.model.MyResponse
import com.example.mytodolist.model.TodoListData
import retrofit2.Call
import retrofit2.http.*

interface TodoInterface {

    @GET("todo")
    fun getData() : Call<MyResponse>

    @GET("todo")
    fun getDataByPage(@Query("page") page : Int,
                      @Query("size") size : Int?) : Call<MyResponse>

    @POST("todo")
    fun addData(@Body todoListData: TodoListData) : Call<MyResponse>

    @POST("todo")
    fun updateData(@Body todoListData: TodoListData) : Call<MyResponse>

}