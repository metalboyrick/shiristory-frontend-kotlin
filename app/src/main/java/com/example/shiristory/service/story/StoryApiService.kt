package com.example.shiristory.service.story

import com.example.shiristory.service.common.Constants
import com.example.shiristory.service.common.Constants.STORY_API_PREFIX
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.story.models.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface StoryApiService {

    // get group list API
    @GET(Constants.STORY_API_PREFIX)
    fun getGroupList(
        @Query("page") page: Int,
        @Query("size") size: Int?
    ): Call<GroupListResponse>

    // get posted stories API
    @GET("${Constants.STORY_API_PREFIX}/{group_id}/stories")
    fun getPostedStories(
            @Path("group_id") group_id: String,
            @Query("page") page: Int,
            @Query("size") size: Int?
    ): Call<StoryEntryResponse>

    // get story group info
    @GET("${Constants.STORY_API_PREFIX}/{group_id}/info")
    fun getGroupInfo(
        @Path("group_id") group_id: String
    ): Call<GroupInfoResponse>

    // delete member
    @HTTP(method = "DELETE", path = "${Constants.STORY_API_PREFIX}/{group_id}/admin/member", hasBody = true)
    fun deleteMember(
        @Path("group_id") group_id: String,
        @Body  jsonBody: String
    ): Call<GenericResponse>

    // upload file
    @Multipart
    @PUT("${Constants.STORY_API_PREFIX}/upload")
    fun uploadFile(
        @Part media: MultipartBody.Part? = null
    ): Call<FileUploadResponse>

    @Headers("Content-Type: application/json")
    @POST("${STORY_API_PREFIX}/create")
    fun createStoryGroup(
        @Body  json_body: String
    ): Call<GroupCreateResponse>
}