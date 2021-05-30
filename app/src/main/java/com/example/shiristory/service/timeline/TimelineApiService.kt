package com.example.shiristory.service.timeline

import com.example.shiristory.service.common.Constants.TIMELINE_API_PREFIX
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.timeline.models.Comment
import com.example.shiristory.service.timeline.models.PostsResponse
import retrofit2.Call
import retrofit2.http.*


interface TimelineApiService {

    @GET("${TIMELINE_API_PREFIX}/view")
    fun getPosts(
        @Query("page") page: Int,
        @Query("size") size: Int?
    ): Call<PostsResponse>

    @Headers("Content-Type: application/json")
    @POST("${TIMELINE_API_PREFIX}/{post_id}/comment")
    fun addComment(
        @Path("post_id") post_id: String,
        @Body  json_body: String
    ): Call<Comment>

}