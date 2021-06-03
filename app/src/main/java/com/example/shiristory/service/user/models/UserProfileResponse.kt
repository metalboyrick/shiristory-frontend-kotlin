package com.example.shiristory.service.user.models
import com.google.gson.annotations.SerializedName


data class UserProfileResponse (
    @SerializedName("user")
    val user: User
)
