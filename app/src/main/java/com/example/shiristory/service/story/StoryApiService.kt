package com.example.shiristory.service.story

import com.example.shiristory.service.common.Constants
import com.example.shiristory.service.common.Constants.TOKEN
import com.example.shiristory.service.story.models.StoryListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface StoryApiService {

    @GET(Constants.STORY_API_PREFIX)
    @Headers("Authorization: $TOKEN")
    fun getGroupList(
        @Query("page") page: Int,
        @Query("size") size: Int?
    ): Call<StoryListResponse>
}