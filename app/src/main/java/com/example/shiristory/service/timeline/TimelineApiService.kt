package com.example.shiristory.service.timeline

import com.example.shiristory.service.common.Constants.TIMELINE_API_PREFIX
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.timeline.models.Comment
import com.example.shiristory.service.timeline.models.Post
import com.example.shiristory.service.timeline.models.PostsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface TimelineApiService {

    @GET("${TIMELINE_API_PREFIX}/view")
    fun getPosts(
        @Query("page") page: Int,
        @Query("size") size: Int?
    ): Call<PostsResponse>

    @Multipart
    @PUT("${TIMELINE_API_PREFIX}/create")
    fun addPost(
        @Part("content") content: RequestBody,
        @Part("media_type") media_type : RequestBody? = null,
        @Part media : MultipartBody.Part? = null
    ): Call<Post>

    @Headers("Content-Type: application/json")
    @POST("${TIMELINE_API_PREFIX}/{post_id}/comment")
    fun addComment(
        @Path("post_id") post_id: String,
        @Body  json_body: String
    ): Call<Comment>

    @POST("${TIMELINE_API_PREFIX}/{post_id}/like")
    fun likePost(
        @Path("post_id") post_id: String
    ): Call<GenericResponse>

    @POST("${TIMELINE_API_PREFIX}/{post_id}/dislike")
    fun dislikePost(
        @Path("post_id") post_id: String
    ): Call<GenericResponse>
}