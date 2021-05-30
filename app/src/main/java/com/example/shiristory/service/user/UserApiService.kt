package com.example.shiristory.service.user

import com.example.shiristory.service.common.Constants
import com.example.shiristory.service.common.Constants.USER_API_PREFIX
import com.example.shiristory.service.common.models.GenericResponse
import com.example.shiristory.service.timeline.models.Comment
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
        @Part("new_nickname") new_nickname: RequestBody ,
        @Part("new_bio") new_bio : RequestBody,
        @Part new_profile_pic : MultipartBody.Part
    ): Call<GenericResponse>
}