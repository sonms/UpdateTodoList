package com.example.mytodolist

import retrofit2.Retrofit

object TodoObject {
    lateinit var client : TodoInterface
    lateinit var retrofit : Retrofit
    private const val BASE_URI = ""
}