package com.example.shiristory.service.story

import com.example.shiristory.service.common.Constants
import com.example.shiristory.service.common.Constants.TOKEN
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.story.models.FileUploadResponse
import com.example.shiristory.service.story.models.GroupInfoResponse
import com.example.shiristory.service.story.models.StoryEntryResponse
import com.example.shiristory.service.story.models.GroupListResponse
import com.example.shiristory.service.timeline.models.Post
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryApiService {

    // get group list API
    @GET(Constants.STORY_API_PREFIX)
    @Headers("Authorization: $TOKEN")
    fun getGroupList(
        @Query("page") page: Int,
        @Query("size") size: Int?
    ): Call<GroupListResponse>

    // get posted stories API
    @GET("${Constants.STORY_API_PREFIX}/{group_id}/stories")
    @Headers("Authorization: $TOKEN")
    fun getPostedStories(
            @Path("group_id") group_id: String,
            @Query("page") page: Int,
            @Query("size") size: Int?
    ): Call<StoryEntryResponse>

    // get story group info
    @GET("${Constants.STORY_API_PREFIX}/{group_id}/info")
    @Headers("Authorization: $TOKEN")
    fun getGroupInfo(
        @Path("group_id") group_id: String
    ): Call<GroupInfoResponse>

    // delete member
    @DELETE("${Constants.STORY_API_PREFIX}/{group_id}/admin/member")
    @Headers("Authorization: $TOKEN")
    fun deleteMember(
        @Path("group_id") group_id: String
    ): Call<GenericResponse>

    // upload file
    @Multipart
    @Headers("Authorization: $TOKEN")
    @PUT("${Constants.STORY_API_PREFIX}/upload")
    fun uploadFile(
        @Part media: MultipartBody.Part? = null
    ): Call<FileUploadResponse>

}