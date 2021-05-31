package com.example.shiristory.service.common

import com.example.shiristory.service.common.Constants.BASE_URL
import com.example.shiristory.service.story.StoryApiService
import com.example.shiristory.service.timeline.TimelineApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitBuilder {

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build() // Doesn't require the adapter
    }

    // Add your api service here
    val timelineApiService: TimelineApiService = getRetrofit().create(TimelineApiService::class.java)
    val storyApiService: StoryApiService = getRetrofit().create(StoryApiService::class.java)

}