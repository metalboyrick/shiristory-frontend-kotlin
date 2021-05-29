package com.example.shiristory.service.user

import com.example.shiristory.service.common.Constants.USER_API_PREFIX
import com.example.shiristory.service.user.models.UserProfileResponse
import retrofit2.Call
import retrofit2.http.GET


interface UserApiService {
    @GET("${USER_API_PREFIX}/profile")
    fun getUserProfile(): Call<UserProfileResponse>
}