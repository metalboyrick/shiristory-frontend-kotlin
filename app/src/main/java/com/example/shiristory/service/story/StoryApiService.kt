package com.example.shiristory.service.story

import com.example.shiristory.service.common.Constants
//import com.example.shiristory.service.common.Constants.TOKEN
import com.example.shiristory.service.story.models.StoryEntryResponse
import com.example.shiristory.service.story.models.GroupListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface StoryApiService {

    // get group list API
    @GET(Constants.STORY_API_PREFIX)
//    @Headers("Authorization: $TOKEN")
    fun getGroupList(
        @Query("page") page: Int,
        @Query("size") size: Int?
    ): Call<GroupListResponse>

    // get posted stories API
    @GET("${Constants.STORY_API_PREFIX}/{group_id}/stories")
//    @Headers("Authorization: $TOKEN")
    fun getPostedStories(
            @Path("group_id") group_id: String,
            @Query("page") page: Int,
            @Query("size") size: Int?
    ): Call<StoryEntryResponse>
}