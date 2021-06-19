package com.example.shiristory.service.authentication

import com.example.shiristory.service.authentication.models.Token
import com.example.shiristory.service.common.Constants.TIMELINE_API_PREFIX
import com.example.shiristory.service.common.Constants.USER_API_PREFIX
import com.example.shiristory.service.common.models.GenericResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


interface AuthenticationApiService {

    @Headers("Content-Type: application/json")
    @POST("${USER_API_PREFIX}/jwt/refresh")
    fun getAccessToken(
        @Body json_body: String
    ): Call<Token>

    @Headers("Content-Type: application/json")
    @POST("${USER_API_PREFIX}/signup")
    fun signUp(
        @Body json_body: String
    ): Call<Token>

    @Headers("Content-Type: application/json")
    @POST("${USER_API_PREFIX}/login")
    fun login(
        @Body json_body: String
    ): Call<Token>

    @POST("${USER_API_PREFIX}/logout")
    fun logout(
    ): Call<GenericResponse>

}