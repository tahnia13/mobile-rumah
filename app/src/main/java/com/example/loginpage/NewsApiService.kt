package com.example.loginpage

import retrofit2.Call
import retrofit2.http.GET

interface NewsApiService {
    @GET("republika/terbaru/")
    fun getTerbaru(): Call<NewsResponse>

    @GET("republika/gaya-hidup/")
    fun getLifestyle(): Call<NewsResponse>
}