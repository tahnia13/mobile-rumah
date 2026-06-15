package com.example.loginpage.data.api

import com.example.loginpage.data.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET

interface NewsApiService {
    @GET("republika/terbaru/")
    fun getTerbaru(): Call<NewsResponse>

    @GET("republika/gaya-hidup/")
    fun getLifestyle(): Call<NewsResponse>
}