package com.example.shiristory.service.user

import com.example.shiristory.service.common.Constants
import com.example.shiristory.service.common.Constants.USER_API_PREFIX
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.timeline.models.Comment
import com.example.shiristory.service.user.models.SearchFriendResponse
import com.example.shiristory.service.user.models.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface UserApiService {
    @GET("${USER_API_PREFIX}/profile")
    fun getUserProfile(): Call<UserProfileResponse>

    @Multipart
    @PUT("${USER_API_PREFIX}/profile")
    fun updateUserProfile(
        @Part("new_nickname") new_nickname: RequestBody,
        @Part("new_bio") new_bio: RequestBody,
        @Part new_profile_pic: MultipartBody.Part
    ): Call<GenericResponse>

    @Multipart
    @PUT("${USER_API_PREFIX}/profile")
    fun updateUserProfile(
        @Part("new_nickname") new_nickname: RequestBody,
        @Part("new_bio") new_bio: RequestBody
    ): Call<GenericResponse>

    @Headers("Content-Type: application/json")
    @POST("${USER_API_PREFIX}/friends/add/{friend_username}")
    fun addFriend(
        @Path("friend_username") friend_username: String
    ): Call<GenericResponse>

    @DELETE("${USER_API_PREFIX}/friends/delete/{friend_id}")
    fun removeFriend(
        @Path("friend_id") friend_id: String
    ): Call<GenericResponse>

    @GET("${USER_API_PREFIX}/friends/search/{friend_nickname}")
    fun searchFriend(
        @Path("friend_nickname") friend_nickname: String
    ): Call<SearchFriendResponse>
}