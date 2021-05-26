package com.example.shiristory.service.timeline

import com.example.shiristory.service.common.Constants.TIMELINE_API_PREFIX
import com.example.shiristory.service.timeline.responses.PostsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface TimelineService {
    @GET("${TIMELINE_API_PREFIX}/view")
    fun getPosts(
        @Query("page") page: Int,
        @Query("size") size: Int?
    ): Call<PostsResponse>
}