package com.example.shiristory.service.common

import com.example.shiristory.service.common.Constants.BASE_URL
import com.example.shiristory.service.timeline.TimelineApiService
import com.example.shiristory.service.user.UserApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build() // Doesn't require the adapter
    }

    // Add your api service here
    val timelineApiService: TimelineApiService = getRetrofit().create(TimelineApiService::class.java)
    val userApiService: UserApiService = getRetrofit().create(UserApiService::class.java)
}