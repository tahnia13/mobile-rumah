package com.example.loginpage.data.api

import com.example.loginpage.data.model.PhotoModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface PhotoApiService {
    @GET("v2/list?limit=10")
    suspend fun getPhotos(): List<PhotoModel>
}

object PhotoApiClient {
    private const val BASE_URL = "https://picsum.photos/"

    val apiService: PhotoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotoApiService::class.java)
    }
}