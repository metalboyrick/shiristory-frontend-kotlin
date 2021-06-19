package com.example.shiristory.service.common

import com.example.shiristory.service.authentication.AuthenticationApiService
import com.example.shiristory.service.common.Constants.BASE_URL
import com.example.shiristory.service.story.StoryApiService
import com.example.shiristory.service.timeline.TimelineApiService
import com.example.shiristory.service.user.UserApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitBuilder {

    private fun getRetrofit(): Retrofit {

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build() // Doesn't require the adapter
    }

    // Add your api service here
    val timelineApiService: TimelineApiService = getRetrofit().create(TimelineApiService::class.java)
    val storyApiService: StoryApiService = getRetrofit().create(StoryApiService::class.java)
    val userApiService: UserApiService = getRetrofit().create(UserApiService::class.java)
    val authenticationApiService: AuthenticationApiService = getRetrofit().create(AuthenticationApiService::class.java)

}