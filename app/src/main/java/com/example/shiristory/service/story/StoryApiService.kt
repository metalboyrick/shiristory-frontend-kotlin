package com.example.shiristory.service.story

import com.example.shiristory.service.common.Constants
import com.example.shiristory.service.story.models.StoryListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface StoryApiService {

    @GET(Constants.STORY_API_PREFIX)
    @Headers("Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjIyNTU5MjA3LCJqdGkiOiJmYzA2ZWJiOGQxYWU0NDQ4OTA1MmM3NDdlYjljZTU2YyIsInVzZXJfaWQiOiJicm9za2kifQ.kngQoBxpKpqtnFfjlAvbwE5cDCcID6wiiMN1ENyfv1Y")
    fun getGroupList(
        @Query("page") page: Int,
        @Query("size") size: Int?
    ): Call<StoryListResponse>
}